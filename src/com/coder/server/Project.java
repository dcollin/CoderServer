package com.coder.server;

import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;

public abstract class Project {
	String name = null;
	CoderProjectType projectType = null;
	CoderCompiler compiler = null;
	
	public Project(){
		
	}
	
	public String[] getFileList(){
		return null;
	}
	
	CoderProjectType getProjectType(){
		return projectType;
	}
}
