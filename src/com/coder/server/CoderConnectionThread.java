package com.coder.server;

import com.coder.server.message.AuthenticationChallengeMsg;
import com.coder.server.message.AuthenticationSuccessMsg;
import com.coder.server.message.CoderMessage;
import com.coder.server.struct.User;
import zutil.Encrypter;
import zutil.Hasher;
import zutil.log.LogUtil;
import zutil.net.threaded.ThreadedTCPNetworkServerThread;
import zutil.parser.json.JSONObjectInputStream;
import zutil.parser.json.JSONObjectOutputStream;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

    private User user;

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
            // TODO: start parsing incoming messages from client
        }catch (Exception e){
            logger.log(Level.SEVERE, "Client Connection issue", e);
        }
        finally {
            close();
        }
    }

    private String authenticate() throws IOException, NoSuchAlgorithmException {
        ///////////// CLEARTEXT CONNECTION //////////////////////
        // We dont create any buffers here as these streams might be replaced by encrypted ones
        in = new JSONObjectInputStream(socket.getInputStream());
        in.registerRootClass(CoderMessage.class);
        out = new JSONObjectOutputStream(socket.getOutputStream());
        out.enableMetaData(false);

        // Receive AuthenticationReq
        CoderMessage msg = readMsg();
        if(msg.AuthenticationReq == null){
            logger.severe("Expected message AuthenticationReq.");
            return null;
        }
        user = UserManager.getInstance().getUser(msg.AuthenticationReq.username);
        if(user == null){
            logger.severe("Unknown user: "+msg.AuthenticationReq.username);
            return null;
        }

        // Send AuthenticationChallenge
        CoderMessage challenge = new CoderMessage();
        challenge.AuthenticationChallenge = new AuthenticationChallengeMsg();
        challenge.AuthenticationChallenge.salt = Integer.toString((int)(Math.random()*1_000_000));
        out.writeObject(challenge);

        // Setting up encryption
        String key = Hasher.PBKDF2(user.getPasswordHash(), challenge.AuthenticationChallenge.salt, 500);
        Encrypter crypto = new Encrypter(key, Encrypter.AES_ALGO);
        in = new JSONObjectInputStream(new BufferedInputStream(crypto.decrypt(socket.getInputStream())));
        in.registerRootClass(CoderMessage.class);
        out = new JSONObjectOutputStream(new BufferedOutputStream(crypto.encrypt(socket.getOutputStream())));
        out.enableMetaData(false);

        ///////////// ENCRYPTED CONNECTION //////////////////////
        // Receive AuthenticationRsp
        msg = readMsg();
        if(msg.AuthenticationRsp == null){
            logger.severe("Expected message AuthenticationRsp.");
            return null;
        }

        // Send AuthenticationSuccessful
        CoderMessage success = new CoderMessage();
        success.AuthenticationSuccess = new AuthenticationSuccessMsg();
        out.writeObject(success);

        return key;
    }


    private CoderMessage readMsg() throws IOException {
        return (CoderMessage) in.readObject();
    }


    public void close(){
        if(socket != null){
            try {
                socket.close();
                socket = null;
            } catch (IOException e1) {
                logger.log(Level.SEVERE, null, e1);
            }
        }
    }
}
