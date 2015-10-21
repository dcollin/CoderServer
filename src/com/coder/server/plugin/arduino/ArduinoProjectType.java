package com.coder.server.plugin.arduino;

import com.coder.server.message.ConfigData;
import com.coder.server.plugin.CoderProjectType;
import com.coder.server.struct.Project;
import com.coder.server.util.ExtendedProperties;
import zutil.log.LogUtil;

import java.util.logging.Logger;

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

	@Override
	public ConfigData getSupportedConfiguration() {
		return null;
	}
}
