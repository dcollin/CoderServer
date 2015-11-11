package com.coder.server;

import com.coder.server.struct.User;
import zutil.log.CompactLogFormatter;
import zutil.log.LogUtil;
import zutil.net.ssdp.SSDPServer;
import zutil.net.ssdp.StandardSSDPInfo;
import zutil.net.threaded.ThreadedTCPNetworkServer;
import zutil.net.threaded.ThreadedTCPNetworkServerThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoderServer extends ThreadedTCPNetworkServer{
    public static final Logger log = LogUtil.getLogger();
    public static final String SERVER_NAME_PROPERTY = "name";
    public static final String SERVER_PORT_PROPERTY = "port";


    public static void main(String[] args){
        try {
            LogUtil.setGlobalLevel(Level.FINEST);
            LogUtil.setGlobalFormatter(new CompactLogFormatter());

            /********* Reading Configuration **********/
            ConfigManager.initialize();
            Properties serverConf = ConfigManager.getInstance().getServerConf();
            if(serverConf == null) { // No server conf found
                serverConf = getDefaultServerConf();
                ConfigManager.getInstance().saveServerConf(serverConf);
            }

            /********* LOAD USER DATA **********/
            log.info("Loading user data...");
            UserManager.initialize();

            /******** LOAD PROJECT DATA *********/
            log.info("Loading project data...");
            FileManager.initialize();
            ProjectManager.initialize();

            /************ JSON ************/
            log.info("Starting up JSON server...");
            CoderServer json = new CoderServer(
                    Integer.parseInt(serverConf.getProperty(SERVER_PORT_PROPERTY)));
            json.start();

            /************ SSDP ************/
            log.info("starting up SSDP server...");
            StandardSSDPInfo service = new StandardSSDPInfo();
            service.setHeader("Server", SERVER_NAME_PROPERTY);
            service.setLocation(InetAddress.getLocalHost().getHostAddress() +":"+ serverConf.getProperty(SERVER_PORT_PROPERTY));
            service.setST("coder:discover");

            SSDPServer ssdp = new SSDPServer();
            ssdp.addService(service);
            ssdp.start();

            log.info("Server up and running.");
        }catch (Exception e){
            log.log(Level.SEVERE, "Unable to start up server.", e);
        }
    }


    public CoderServer(int port) throws Exception{
        super(port);
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


    public static Properties getDefaultServerConf(){
        Properties conf = new Properties();
        conf.setProperty(SERVER_NAME_PROPERTY, "Coder Server");
        conf.setProperty(SERVER_PORT_PROPERTY, "1337");
        return conf;
    }
}
