package com.coder.server.struct;

import com.coder.server.ProjectManager;
import com.coder.server.message.ConfigData;
import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;

import java.util.List;
import java.util.Properties;

public abstract class Project {
	private String name = null;
	private String description = null;
	private CoderProjectType projectType = null;
	
	public Project(String name, CoderProjectType projectType){
		this.name = name;
		this.projectType = projectType;
	}
	public Project(Properties userProp) {
		this.name = userProp.getProperty("name");
		this.description = userProp.getProperty("description");
		this.projectType = ProjectManager.getInstance().getProjectType(userProp.getProperty("type"));
	}
	public Properties getProperties(){
		Properties userProp = new Properties();
		userProp.setProperty("name", name);
		userProp.setProperty("description", description);
		userProp.getProperty("type", projectType.getName());
		return userProp;
	}

	
	public List<String> getFileList(){
		return null;
	}
	
	public CoderProjectType getProjectType(){
		return projectType;
	}
	
	public String getName(){
		return this.name;
	}

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



    /**
     * @return a compiler for the specific project and project type
     */
    public abstract CoderCompiler getCompiler();

	/**
	 * @return config data specific for the project type
	 */
	public abstract ConfigData getConfiguration();

	/**
	 * @param 	data    Configure the project with the specific project type data.
	 */
	public abstract void setConfiguration(ConfigData data);
}
