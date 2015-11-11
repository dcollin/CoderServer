package com.coder.server;

import com.coder.server.struct.Project;
import zutil.io.file.FileSearcher;
import zutil.log.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles all project file related functions
 */
public class FileManager {
    private static final Logger logger = LogUtil.getLogger();
    private static FileManager instance;


    private FileManager() {}


    public File getFile(Project project, String file){
        File root = ConfigManager.getInstance().getProjectRoot(project);
        if(root != null){
            return new File(root, file);
        }
        return null;
    }

    public List<String> getProjectFileList(Project project) throws IOException {
        ArrayList<String> fileList = new ArrayList<>();
        File projectRoot = ConfigManager.getInstance().getProjectRoot(project);
        FileSearcher search = new FileSearcher(projectRoot);
        search.searchFolders(false);
        search.setRecursive(true);
        search.searchCompressedFiles(false);
        String rootStr = projectRoot.getCanonicalPath();
        for(FileSearcher.FileSearchItem file : search){
            if(file.getPath().startsWith(rootStr)){
                String path = file.getPath().substring(rootStr.length());
                fileList.add(path);
            }
            else
                logger.severe("File not under project root! Project: '"+ rootStr +"' File: '"+ file.getPath() +"'");
        }
        return fileList;
    }


    public static FileManager getInstance(){
        return instance;
    }

    public static synchronized void initialize(){
        instance = new FileManager();
    }
}
