package com.coder.server.plugin.arduino;

import com.coder.server.Project;
import com.coder.server.plugin.CoderProjectType;

public class ArdunioProjectType implements CoderProjectType {

	@Override
	public void init() {

	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getArchitecture() {
		return null;
	}

	@Override
	public Project createProject() {
		return new ArduinoProject();
	}

	@Override
	public Project loadProject(String name) {
		return null;
	}
	
}
