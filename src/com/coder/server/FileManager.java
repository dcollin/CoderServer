package com.coder.server;

import com.coder.server.struct.Project;
import zutil.log.LogUtil;

import java.io.File;
import java.util.logging.Logger;

/**
 * Handles all project files
 */
public class FileManager {
    private static final Logger logger = LogUtil.getLogger();
    private static FileManager instance;


    private FileManager() {}


    public File getFile(Project proj, String file){
        File root = ConfigManager.getInstance().getProjectRoot(proj);
        if(root != null){
            return new File(root, file);
        }
        return null;
    }


    public static FileManager getInstance(){
        return instance;
    }

    public static synchronized void initialize(){

    }
}
