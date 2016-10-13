package com.coder.server;

import com.coder.server.plugin.CoderProjectType;
import com.coder.server.plugin.generic.GenericProjectType;
import com.coder.server.struct.Project;
import com.coder.server.struct.User;
import zutil.log.LogUtil;
import zutil.plugin.PluginData;
import zutil.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class managing all projects
 */
public class ProjectManager implements Iterable<Project>{
    private static final Logger logger = LogUtil.getLogger();
    private static ProjectManager instance;

    private HashMap<String,CoderProjectType> projectTypes = new HashMap<>();
    private HashMap<String,Project> projects = new HashMap<>();


    private ProjectManager(){ }


    public CoderProjectType getProjectType(String name){
        return projectTypes.get(name);
    }
    private void addProjectType(CoderProjectType projType){
        projectTypes.put(projType.getName(), projType);
    }
    public Iterator<CoderProjectType> getProjectTypeIterator(){
        return projectTypes.values().iterator();
    }

    public void addProject(Project proj){
        projects.put(proj.getName(), proj);
    }
    public Project getProject(String name){
        return projects.get(name);
    }
    @Override
    public Iterator<Project> iterator(){
        return projects.values().iterator();
    }

    public void saveProject(Project project) throws IOException {
        logger.info("Saving project('"+ project.getName() +"') config...");
        Properties projProp = project.getConfiguration();
        if(projProp == null)
            projProp = new Properties();
        projProp.setProperty(Project.PROJECT_NAME_PROPERTY, project.getName());
        projProp.setProperty(Project.PROJECT_TYPE_PROPERTY, project.getProjectType().getName());
        if(project.getDescription() != null)
            projProp.setProperty(Project.PROJECT_DESC_PROPERTY, project.getDescription());

        ConfigManager.getInstance().saveProjectConf(project, projProp);
    }


    public static ProjectManager getInstance(){
        return instance;
    }

    protected static synchronized void initialize() throws IOException {
        ProjectManager newInstance = new ProjectManager();

        /*********** LOAD PLUGINS **********/
        logger.info("Loading project type plugins...");
        newInstance.addProjectType(new GenericProjectType());
        PluginManager<?> projPlugins = new PluginManager<>();
        for(PluginData plugin : projPlugins){
            for(Iterator<CoderProjectType> it = plugin.getObjectIterator(CoderProjectType.class); it.hasNext();){
                CoderProjectType p = it.next();
                logger.info("Found project type: " + p.getName());
                newInstance.addProjectType(p);
                p.init();
            }
        }

        /*********** LOAD PROJECTS **********/
        logger.info("Loading projects...");
        for(Properties projProp : ConfigManager.getInstance().getProjectConfs()) {
            logger.info("Found project: " + projProp.getProperty(Project.PROJECT_NAME_PROPERTY));
            CoderProjectType projType = newInstance.getProjectType(projProp.getProperty(Project.PROJECT_TYPE_PROPERTY));
            if(projType == null){
                logger.severe("Unknown ProjectType: " + projProp.getProperty(Project.PROJECT_TYPE_PROPERTY));
                continue;
            }
            Project proj = projType.createProject(projProp.getProperty(Project.PROJECT_NAME_PROPERTY));
            if(projProp.containsKey(Project.PROJECT_DESC_PROPERTY))
                proj.setDescription(projProp.getProperty(Project.PROJECT_DESC_PROPERTY));
            proj.setConfiguration(projProp);
            List<String> fileList = FileManager.getInstance().getProjectFileList(proj);
            for(String file : fileList)
                proj.getFileList().add(file);
            newInstance.addProject(proj);
        }

        instance = newInstance;
    }
}
