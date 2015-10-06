package com.coder.server.struct;

import java.io.File;

import com.coder.server.message.ConfigData;
import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;

public abstract class Project implements CoderCompiler {
	private String name = null;
	private CoderProjectType projectType = null;
	
	public Project(String name, CoderProjectType projectType){
		this.name = name;
		this.projectType = projectType;
	}
	
	public File[] getFileList(){
		return null;
	}
	
	public CoderProjectType getProjectType(){
		return projectType;
	}
	
	public String getName(){
		return this.name;
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
