package com.coder.server.plugin;

import com.coder.server.Project;

/**
 * Plugin interface
 */
public interface CoderProjectType {
	public void init();
	public String getName();
	public String getArchitecture();
	public Project createProject();
	public Project loadProject(String name);
}