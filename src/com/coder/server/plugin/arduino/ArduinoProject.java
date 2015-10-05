package com.coder.server.plugin.arduino;

import com.coder.server.plugin.CoderProjectType;
import com.coder.server.plugin.CompileStatusListener;
import com.coder.server.plugin.ExecInstance;
import com.coder.server.plugin.arduino.struct.Board;
import com.coder.server.struct.Project;
import com.coder.server.util.ExtendedProperties;
import com.fazecast.jSerialComm.SerialPort;

public class ArduinoProject extends Project {
	
	private Board targetBoard;
	private SerialPort targetPort;
	private ArduinoCompiler compiler;
	private ExtendedProperties projectProperties;
	
	public ArduinoProject(String name, CoderProjectType projectType, ArduinoCompiler compiler){
		super(name, projectType);
		this.compiler = compiler;
		projectProperties = new ExtendedProperties();
		projectProperties.setProperty("build.project_name", name+".cpp");
	}
	
	/**
	 * @return The combined properties of the project, project type,  compiler and target board
	 */
	public ExtendedProperties getBuildProperties(){
		//get compiler properties
		if(compiler == null){
			return null;
		}
		ExtendedProperties compilerProperties = compiler.getProperties();
		
		//get target board properties
		if(targetBoard == null){
			return null;
		}
		ExtendedProperties targetBoardProperties = this.targetBoard.getProperties();
		
		//get project properties
		if(this.projectProperties == null){
			return null;
		}
		
		//get project type properties
		CoderProjectType projectType = getProjectType();
		if( projectType == null || ((projectType instanceof ArduinoProjectType) == false) ){
			return null;
		}
		ArduinoProjectType arduinoProjectType = (ArduinoProjectType)projectType;
		ExtendedProperties projectTypeProperties = arduinoProjectType.getProperties();
		
		//combine all properties into one
		ExtendedProperties buildProperties = ExtendedProperties.combine(projectTypeProperties, this.projectProperties, compilerProperties, targetBoardProperties);
		return buildProperties;
	}

	@Override
	public boolean compile(CompileStatusListener listener) {
		return this.compiler.compile(this, listener);
	}

	@Override
	public ExecInstance createExecInstance() {
		return this.compiler.createExecInstance(this);
	}

	public SerialPort getTargetPort() {
		return this.targetPort;
	}
}
