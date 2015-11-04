package com.coder.server.plugin;

import com.coder.server.message.SupportedProperties;
import com.coder.server.struct.Project;

/**
 * Plugin interface
 */
public interface CoderProjectType {

	public void init();

	public String getName();

    public SupportedProperties getSupportedConfiguration();

	public Project createProject(String name);

	public Project getProject(String name);
}