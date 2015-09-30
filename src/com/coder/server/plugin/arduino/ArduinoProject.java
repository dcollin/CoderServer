package com.coder.server.plugin.arduino;

import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;
import com.coder.server.plugin.arduino.struct.Board;
import com.coder.server.struct.Project;
import com.coder.server.util.ExtendedProperties;

public class ArduinoProject extends Project {
	
	private Board targetBoard;
	private String targetPort;
	private ExtendedProperties projectProperties = new ExtendedProperties();
	
	public ArduinoProject(String name, CoderProjectType projectType, CoderCompiler compiler){
		super(name, projectType, compiler);
		projectProperties.setProperty("build.project_name", name+".cpp");
	}
	
	/**
	 * @return The combined properties of the project, project type,  compiler and target board
	 */
	public ExtendedProperties getBuildProperties(){
		//get compiler properties
		CoderCompiler compiler = getCompiler();
		if( (compiler instanceof ArduinoCompiler) == false ){
			return null;
		}
		ArduinoCompiler arduinoCompiler = (ArduinoCompiler)compiler;
		ExtendedProperties compilerProperties = arduinoCompiler.getProperties();
		
		//get target board properties
		if(targetBoard == null){
			return null;
		}
		ExtendedProperties targetBoardProperties = this.targetBoard.getProperties();
		
		//get project type properties
		CoderProjectType projectType = getProjectType();
		if( (projectType instanceof ArduinoProjectType) == false){
			return null;
		}
		ArduinoProjectType arduinoProjectType = (ArduinoProjectType)projectType;
		ExtendedProperties projectTypeProperties = arduinoProjectType.getProperties();
		
		//combine all properties into one
		ExtendedProperties buildProperties = ExtendedProperties.combine(projectTypeProperties, this.projectProperties, compilerProperties, targetBoardProperties);
		return buildProperties;
	}
		
}
