package com.coder.server;

import com.coder.server.struct.Project;
import zutil.log.LogUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String USER_CONF_EXT = ".properties";

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
            serverConf = new Properties(getDefaultServerConf());
            serverConfFile.getParentFile().mkdirs();
            FileWriter out = new FileWriter(serverConfFile);
            getDefaultServerConf().store(out, "This is a auto generated config file");
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


    public Properties getUserConf(String username) throws IOException {
        return getUserConf(
                new File(userPath, username+USER_CONF_EXT));
    }
    private Properties getUserConf(File userConf) throws IOException {
        if(userConf.isFile()) { // is there a userConf file
            Properties prop = new Properties();
            FileReader in = new FileReader(userConf);
            prop.load(in);
            in.close();
            return prop;
        }
        return null;
    }
    public List<Properties> getUserConfs() throws IOException {
        ArrayList<Properties> list = new ArrayList();
        for(File userConf : userPath.listFiles()){
            if(userConf.isFile()){
                list.add(getUserConf(userConf));
            }
        }
        return list;
    }


    public File getProjectRoot(Project project){
        return getProjectRoot(project.getName());
    }
    public File getProjectRoot(String projectName){
        return new File(projectPath, cleanString(projectName));
    }
    public Properties getProjectConf(String projectName) throws IOException {
        return getProjectConf(getProjectConfFile(projectName));
    }
    public File getProjectConfFile(String projectName){
        return new File(getProjectRoot(projectName), PROJECT_CONF);
    }
    private Properties getProjectConf(File projConf) throws IOException {
        if(projConf.isFile()) { // is there a config file?
            Properties prop = new Properties();
            FileReader in = new FileReader(projConf);
            prop.load(in);
            in.close();
            return prop;
        }
        return null;
    }
    public List<Properties> getProjectConfs() throws IOException {
        ArrayList<Properties> list = new ArrayList();
        for(File projDir : projectPath.listFiles()){
            File projConf = new File(projDir, PROJECT_CONF);
            if(projConf.exists() && projConf.isFile()){
                list.add(getProjectConf(projConf));
            }
            else
                logger.warning("Unable to find project properties file: "+ projDir);

        }
        return list;
    }
    public void saveProjectConf(Project project, Properties projProp) throws IOException {
        File root = getProjectRoot(project);
        if(!root.isDirectory())
            root.mkdirs();
        File propFile = getProjectConfFile(project.getName());
        FileWriter out = new FileWriter(propFile);
        projProp.store(out, null);
        out.close();
    }



    private static Properties getDefaultServerConf(){
        Properties conf = new Properties();
        conf.setProperty("port", "1337");
        return conf;
    }


    private static String cleanString(String str){
        str = str.replaceAll("\\W", "_");
        return str.toLowerCase();
    }



    protected static void initialize() throws IOException {
        if(instance == null)
            instance = new ConfigManager();
    }

    public static ConfigManager getInstance(){
        return instance;
    }


}
