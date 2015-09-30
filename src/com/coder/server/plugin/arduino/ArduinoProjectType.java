package com.coder.server.plugin.arduino;

import com.coder.server.struct.Project;
import com.coder.server.util.ExtendedProperties;
import com.coder.server.plugin.CoderProjectType;

public class ArduinoProjectType implements CoderProjectType {
	
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
