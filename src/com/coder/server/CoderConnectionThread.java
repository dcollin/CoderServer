package com.coder.server;

import com.coder.server.message.*;
import com.coder.server.plugin.CoderProjectType;
import com.coder.server.struct.Project;
import com.coder.server.struct.User;
import zutil.Hasher;
import zutil.io.MultiPrintStream;
import zutil.log.InputStreamLogger;
import zutil.log.LogUtil;
import zutil.log.OutputStreamLogger;
import zutil.net.threaded.ThreadedTCPNetworkServerThread;
import zutil.parser.json.JSONObjectInputStream;
import zutil.parser.json.JSONObjectOutputStream;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ziver on 2015-09-29.
 */
public class CoderConnectionThread implements ThreadedTCPNetworkServerThread {
    private static final Logger logger = LogUtil.getLogger();

    private Socket socket;
    private JSONObjectInputStream in;
    private JSONObjectOutputStream out;

    private User user = null;


    public CoderConnectionThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Authentication
            String key = authenticate();
            if(socket == null || key == null)
                return;

            // Handle incoming messages
            CoderMessage msg = null;
            while((msg=in.readGenericObject()) != null){
                // DEBUG: Dump the received message to the terminal
                //MultiPrintStream.out.dump(msg);

                CoderMessage rspMsg = new CoderMessage();
                //********* PROJECT HANDLING *********
                if(msg.ProjectTypeReq != null){
                    rspMsg.ProjectTypeRsp = new HashMap<>();
                    // Send a single project type
                    if(msg.ProjectTypeReq.type != null){
                        CoderProjectType type = ProjectManager.getInstance()
                                .getProjectType(msg.ProjectTypeReq.type);
                        if(type != null)
                            rspMsg.ProjectTypeRsp.put(type.getName(), type.getSupportedConfiguration());
                    }
                    // Send all project types
                    else {
                        for (Iterator<CoderProjectType> it=ProjectManager.getInstance().getProjectTypeIterator();
                                it.hasNext();) {
                            CoderProjectType type = it.next();
                            rspMsg.ProjectTypeRsp.put(type.getName(), type.getSupportedConfiguration());
                        }
                    }
                }
                if(msg.ProjectListReq != null){
                    rspMsg.ProjectListRsp = new HashMap<>();
                    for(Project proj : ProjectManager.getInstance()){
                        ProjectListData data = new ProjectListData();
                        data.type = proj.getProjectType().getName();
                        data.description = proj.getDescription();
                        rspMsg.ProjectListRsp.put(proj.getName(), data);
                    }
                }
                if(msg.ProjectReq != null || msg.ProjectCreateReq != null){
                    rspMsg.ProjectRsp = new ProjectRspMsg();
                    Project proj = null;
                    // Create new project
                    if(msg.ProjectCreateReq != null){
                        CoderProjectType type = ProjectManager.getInstance()
                                .getProjectType(msg.ProjectCreateReq.type);
                        if(type != null) {
                            proj = type.createProject(msg.ProjectCreateReq.name);
                            if(msg.ProjectCreateReq.description != null)
                                proj.setDescription(msg.ProjectCreateReq.description);
                            ProjectManager.getInstance().addProject(proj);
                            ProjectManager.getInstance().saveProject(proj);
                        }
                        else
                            rspMsg.ProjectRsp.error = "No such project type found.";
                    }
                    // Get existing project
                    else
                        proj = ProjectManager.getInstance().getProject(msg.ProjectReq.name);

                    if(proj != null){
                        rspMsg.ProjectRsp.name = proj.getName();
                        rspMsg.ProjectRsp.type = proj.getProjectType().getName();
                        rspMsg.ProjectRsp.description = proj.getDescription();
                        rspMsg.ProjectRsp.config = proj.getConfiguration();
                        rspMsg.ProjectRsp.fileList = proj.getFileList();
                    }
                    else if(rspMsg.ProjectRsp.error == null) // Do we already have a error msg?
                        rspMsg.ProjectRsp.error = "No such project found.";
                }

                out.writeObject(rspMsg);
                out.flush();
            }
        }catch (Exception e){
            logger.log(Level.SEVERE, null, e);
        }
        finally {
            close();
        }
    }


    private String authenticate() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException {
        ///////////// CLEARTEXT CONNECTION //////////////////////
        // We don't create any buffers here as these streams might be replaced by encrypted ones
        in = new JSONObjectInputStream(new InputStreamLogger(socket.getInputStream()));
        in.registerRootClass(CoderMessage.class);
        out = new JSONObjectOutputStream(new OutputStreamLogger(socket.getOutputStream()));
        out.enableMetaData(false);

        // Receive AuthenticationReq
        CoderMessage msg = in.readGenericObject();
        if(msg == null || msg.AuthenticationReq == null){
            logger.severe("Expected message AuthenticationReq.");
            return null;
        }
        user = UserManager.getInstance().getUser(msg.AuthenticationReq.username);
        if(user == null){
            logger.severe("Unknown user: '"+ msg.AuthenticationReq.username +"'");
            return null;
        }

        // Send AuthenticationChallenge
        String salt = Integer.toString((int)(Math.random()*1_000_000));
        CoderMessage challenge = new CoderMessage();
        challenge.AuthenticationChallenge = new AuthenticationChallengeMsg();
        challenge.AuthenticationChallenge.salt = salt;
        out.writeObject(challenge);
        out.flush();

        // Setting up encryption
        String key = Hasher.PBKDF2(user.getPasswordHash(), challenge.AuthenticationChallenge.salt, 500);
        /*Encrypter crypto = new Encrypter(key, Encrypter.Algorithm.AES);
        in = new JSONObjectInputStream(new BufferedInputStream(crypto.decrypt(new InputStreamLogger(socket.getInputStream()))));
        in.registerRootClass(CoderMessage.class);
        out = new JSONObjectOutputStream(new BufferedOutputStream(crypto.encrypt(new OutputStreamLogger(socket.getOutputStream()))));
        out.enableMetaData(false);
        */

        ///////////// ENCRYPTED CONNECTION //////////////////////
        // Receive AuthenticationRsp
        msg = in.readGenericObject();
        if(msg == null || msg.AuthenticationRsp == null){
            logger.severe("Expected message AuthenticationRsp.");
            return null;
        }
        String hash = Hasher.PBKDF2(user.getPasswordHash(), salt, 500);
        if(!hash.equals(msg.AuthenticationRsp.hash)){
            logger.severe("Wrong AuthenticationRsp hash provided: '"+msg.AuthenticationRsp.hash+"' (Expected: '"+hash+"')");
            return null;
        }

        // Send AuthenticationSuccessful
        CoderMessage success = new CoderMessage();
        success.AuthenticationSuccess = new AuthenticationSuccessMsg();
        out.writeObject(success);
        out.flush();

        logger.info("User '" + user.getUsername() + "' has connected from ip: " + socket.getInetAddress());
        return key;
    }


    public void close(){
        if(socket != null){
            try {
                logger.info("Disconnecting"+ (user!=null? (" user '" + user.getUsername() + "' with"):"") +" ip: " + socket.getInetAddress());
                socket.close();
                socket = null;
            } catch (IOException e1) {
                logger.log(Level.SEVERE, null, e1);
            }
        }
    }
}
