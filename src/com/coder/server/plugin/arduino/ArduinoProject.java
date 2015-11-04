package com.coder.server.plugin.arduino;

import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;
import com.coder.server.plugin.ExecInstance;
import com.coder.server.plugin.ExecListener;
import com.coder.server.plugin.arduino.struct.Board;
import com.coder.server.struct.Project;
import com.coder.server.util.ExtendedProperties;
import zutil.log.LogUtil;

import java.util.Properties;
import java.util.logging.Logger;

public class ArduinoProject extends Project implements CoderCompiler{
	private static final Logger logger = LogUtil.getLogger();
	
	private Board targetBoard;
	private String targetPort;
	private ArduinoCompiler compiler;
	private ExtendedProperties projectProperties;
	private int targetBauderate;
	
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
	public CoderCompiler getCompiler() {
		return this;
	}

	@Override
	public CompileStatus compile(ExecListener listener) {
		return this.compiler.compile(this, listener);
	}

	@Override
	public ExecInstance createExecInstance() {
		return this.compiler.createExecInstance(this);
	}

	public String getTargetPort() {
		return this.targetPort;
	}
	
	public int getTargetBaudrate(){
		return this.targetBauderate;
	}


	@Override
	public Properties getConfiguration() {
		Properties prop = new Properties();
		//prop.setProperty("target_board", targetBoard.getName());
		prop.setProperty("target_port", ""+targetPort);
		prop.setProperty("target_baud", ""+targetBauderate);
		return null;
	}

	@Override
	public void setConfiguration(Properties prop) {
		//targetBoard = prop.getProperty("target_board");
		if(prop.contains("target_port")) targetPort = prop.getProperty("target_port");
        if(prop.contains("target_baud")) targetBauderate = Integer.parseInt(prop.getProperty("target_baud"));
	}
}
