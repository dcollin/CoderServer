package com.coder.server;

import com.coder.server.plugin.CoderProjectType;
import com.coder.server.struct.User;
import zutil.log.CompactLogFormatter;
import zutil.log.LogUtil;
import zutil.net.ssdp.SSDPServer;
import zutil.net.ssdp.StandardSSDPInfo;
import zutil.net.threaded.ThreadedTCPNetworkServer;
import zutil.net.threaded.ThreadedTCPNetworkServerThread;
import zutil.plugin.PluginData;
import zutil.plugin.PluginManager;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoderServer extends ThreadedTCPNetworkServer{
    public static final Logger log = LogUtil.getLogger();
    public static final int SERVER_PORT = 1337;

    private static HashMap<String,CoderProjectType> projectTypes = new HashMap<>();


    public static void main(String[] args){
        try {
            LogUtil.setGlobalLevel(Level.FINEST);
            LogUtil.setGlobalFormatter(new CompactLogFormatter());

            /*********** PLUGINS **********/
            log.info("Looking for plugins...");
            PluginManager<?> projPlugins = new PluginManager<>();
            for(PluginData plugin : projPlugins){
                for(Iterator<CoderProjectType> it = plugin.getIterator(CoderProjectType.class); it.hasNext();){
                    CoderProjectType p = it.next();
                    projectTypes.put(p.getName(), p);
                }
            }

            /********* LOAD DATA **********/
            log.info("Loading configuration data...");
            // TODO: add actual database/file setup
            UserManager.initialize();
            User user1 = new User("ziver");
            user1.setPassword("bytesut");
            UserManager.getInstance().addUser(user1);
            User user2 = new User("daniel");
            user2.setPassword("bytesut");
            UserManager.getInstance().addUser(user2);

            /************ JSON ************/
            log.info("Starting up JSON server...");
            CoderServer json = new CoderServer();
            json.start();

            /************ SSDP ************/
            log.info("starting up SSDP server...");
            StandardSSDPInfo service = new StandardSSDPInfo();
            service.setLocation("nowhere");
            service.setST("coder:discover");

            SSDPServer ssdp = new SSDPServer();
            ssdp.addService(service);
            ssdp.start();

            log.info("Server up and running.");
        }catch (Exception e){
            log.log(Level.SEVERE, "Unable to start up server.", e);
        }
    }


    public CoderServer() {
        super(SERVER_PORT);
    }

    @Override
    protected ThreadedTCPNetworkServerThread getThreadInstance(Socket socket) {
        try {
            return new CoderConnectionThread(socket);
        }catch(IOException e){
            log.log(Level.SEVERE, null, e);
        }
        return null;
    }
}
