package com.coder.server.struct;

import java.io.File;

import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;

public abstract class Project {
	private String name = null;
	private CoderProjectType projectType = null;
	private CoderCompiler compiler = null;
	
	public Project(String name, CoderProjectType projectType, CoderCompiler projectCompiler){
		this.name = name;
		this.projectType = projectType;
		this.compiler = projectCompiler;
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
	
	public CoderCompiler getCompiler(){
		return this.compiler;
	}
	
}
