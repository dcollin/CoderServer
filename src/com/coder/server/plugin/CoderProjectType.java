package com.coder.server.plugin;

import com.coder.server.struct.Project;

/**
 * Plugin interface
 */
public interface CoderProjectType {
	public void init();
	public String getName();
	public String[] getSupportedArchitectures();
	public Project createProject(String name);
	public Project getProject(String name);
}