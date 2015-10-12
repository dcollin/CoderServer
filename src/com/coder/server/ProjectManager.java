package com.coder.server;

import com.coder.server.plugin.CoderProjectType;
import com.coder.server.struct.Project;
import zutil.log.LogUtil;
import zutil.plugin.PluginData;
import zutil.plugin.PluginManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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


    public HashMap<String,CoderProjectType> getProjectTypes(){
        return projectTypes;
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

    protected static synchronized void initialize(){
        ProjectManager newInstance = new ProjectManager();

        /*********** LOAD PLUGINS **********/
        logger.info("Loading project plugins...");
        PluginManager<?> projPlugins = new PluginManager<>();
        for(PluginData plugin : projPlugins){
            for(Iterator<CoderProjectType> it = plugin.getIterator(CoderProjectType.class); it.hasNext();){
                CoderProjectType p = it.next();
                newInstance.projectTypes.put(p.getName(), p);
            }
        }

        /*********** LOAD PLUGINS **********/
        logger.info("Loading projects...");
        // TODO: load project duh!

        instance = newInstance;
    }
}
