package com.coder.server.plugin.arduino;

import java.util.logging.Logger;

import zutil.log.LogUtil;

import com.coder.server.struct.Project;
import com.coder.server.util.ExtendedProperties;
import com.coder.server.plugin.CoderProjectType;

public class ArduinoProjectType implements CoderProjectType {
	private static final Logger logger = LogUtil.getLogger();
	
	ExtendedProperties projectTypeProperties = new ExtendedProperties();
	
	@Override
	public String getName() {
		return "Arduino";
	}

	@Override
	public void init() {
		
	}

	@Override
	public String[] getSupportedArchitectures() {
		return null;
	}

	@Override
	public Project createProject(String name) {
		return new ArduinoProject(name, this, new ArduinoCompiler());
	}

	@Override
	public Project getProject(String name) {
		return null;
	}
	
	public ExtendedProperties getProperties(){
		return this.projectTypeProperties;
	}
		
}
