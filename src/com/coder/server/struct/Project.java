package com.coder.server.struct;

import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;

import java.util.List;
import java.util.Properties;

public abstract class Project {
    public static final String PROJECT_NAME_PROPERTY = "name";
    public static final String PROJECT_DESC_PROPERTY = "description";
    public static final String PROJECT_TYPE_PROPERTY = "type";

	private String name = null;
	private String description = null;
	private CoderProjectType projectType = null;
	
	public Project(String name, CoderProjectType projectType){
		this.name = name;
		this.projectType = projectType;
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
     * Should be overridden by ProjectType specific project and set proprietary configuration
     *
     * @param 	userProp    Configure the project with the specific project type data.
     */
    public void setConfiguration(Properties userProp) { };
    /**
     * Should be overridden by ProjectType specific project and return proprietary configuration
     *
     * @return config data specific for the project type (default: null)
     */
    public Properties getConfiguration() { return null; };

    /**
     * @return a compiler for the specific project and project type
     */
    public abstract CoderCompiler getCompiler();

}
