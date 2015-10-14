package com.coder.server;

import zutil.log.LogUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class handles all configuration for the Coder Server
 */
public class ConfigManager {
    // Folders
    private static final String ROOT_PATH = "./";
    private static final String USER_PATH = "user/";
    private static final String PROJECT_PATH = "project/";

    // Configuration files
    private static final String SERVER_CONF = "coder.properties";
    private static final String PROJECT_CONF = ".project";

    private static final Logger logger = LogUtil.getLogger();
    private static ConfigManager instance;

    // Object fields
    private File userPath;
    private File projectPath;
    private Properties serverConf;


    private ConfigManager() throws IOException {
        File rootPath    = new File(ROOT_PATH);
        this.userPath    = new File(rootPath, USER_PATH);
        this.projectPath = new File(rootPath, PROJECT_PATH);

        File serverConfFile = new File(rootPath, SERVER_CONF);
        if(serverConfFile.exists() && serverConfFile.isFile()) {
            serverConf = new Properties();
            FileReader in = new FileReader(serverConfFile);
            serverConf.load(in);
            in.close();
        }
        else{
            serverConf = getDefaultServerConf();
            serverConfFile.getParentFile().mkdirs();
            FileWriter out = new FileWriter(serverConfFile);
            serverConf.store(out, "This is a auto generated config file");
            out.close();
        }

        // Create folders
        if(!this.userPath.exists())
            this.userPath.mkdirs();
        if(!this.projectPath.exists())
            this.projectPath.mkdirs();
    }

    public Properties getServerConf(){
        return serverConf;
    }

    public List<Properties> getProjectConfs() throws IOException {
        ArrayList<Properties> list = new ArrayList();
        for(File projDir : projectPath.listFiles()){
            File projConf = new File(projDir, PROJECT_CONF);
            if(projConf.exists() && projConf.isFile()){
                Properties prop = new Properties();
                FileReader in = new FileReader(projConf);
                prop.load(in);
                in.close();
                list.add(prop);
            }
            else
                logger.warning("Unable to find project properties file: "+ projDir);

        }
        return list;
    }



    private static Properties getDefaultServerConf(){
        Properties conf = new Properties();
        conf.setProperty("port", "1337");
        return conf;
    }
    private static Properties getDefaultUserConf(){
        Properties conf = new Properties();
        conf.setProperty("username", "nobody");
        conf.setProperty("passhash", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        return conf;
    }
    private static Properties getDefaultProjectConf(){
        Properties conf = new Properties();
        conf.setProperty("name", "New Project");
        conf.setProperty("type", "Generic");
        return conf;
    }

    private static String cleanString(String str){
        str = str.replaceAll("\\W", "_");
        return str;
    }



    protected static void initialize() throws IOException {
        if(instance == null)
            instance = new ConfigManager();
    }

    public static ConfigManager getInstance(){
        return instance;
    }
}
