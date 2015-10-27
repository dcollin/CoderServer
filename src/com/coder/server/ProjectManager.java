package com.coder.server;

import com.coder.server.plugin.CoderProjectType;
import com.coder.server.plugin.generic.GenericProjectType;
import com.coder.server.struct.Project;
import com.coder.server.struct.User;
import zutil.log.LogUtil;
import zutil.plugin.PluginData;
import zutil.plugin.PluginManager;

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

    private HashMap<String,CoderProjectType> projectTypes;
    private HashMap<String,Project> projects;


    private ProjectManager(){
        projectTypes = new HashMap<>();
        projects = new HashMap<>();
    }


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
            for(Iterator<CoderProjectType> it = plugin.getIterator(CoderProjectType.class); it.hasNext();){
                CoderProjectType p = it.next();
                logger.info("Found project type: " + p.getName());
                newInstance.addProjectType(p);
            }
        }

        /*********** LOAD PLUGINS **********/
        logger.info("Loading projects...");
        for(Properties projProp : ConfigManager.getInstance().getProjectConfs()) {
            logger.info("Found project: " + projProp.getProperty(Project.PROJECT_NAME_PROPERTY));
            CoderProjectType projType = newInstance.getProjectType(projProp.getProperty(Project.PROJECT_TYPE_PROPERTY));
            if(projType == null){
                logger.severe("Unknown ProjectType: " + projProp.getProperty(Project.PROJECT_TYPE_PROPERTY));
                continue;
            }
            Project proj = projType.createProject(projProp.getProperty(Project.PROJECT_NAME_PROPERTY));
            proj.setProperties(projProp);
            newInstance.addProject(proj);
        }

        instance = newInstance;
    }
}
