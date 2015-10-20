package com.coder.server.struct;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.coder.server.ProjectManager;
import com.coder.server.message.ConfigData;
import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;

public abstract class Project implements CoderCompiler {
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
	 * @return the current configuration of the project, should always return a valid object
	 */
	public abstract ConfigData getConfiguration();

	/**
	 * Configure the project with the specific ConfigData.
	 * @param 	data
	 */
	public abstract void setConfiguration(ConfigData data);

}
