package com.coder.server.plugin.generic;

import com.coder.server.message.SupportedProperties;
import com.coder.server.plugin.CoderProjectType;
import com.coder.server.struct.Project;

import java.util.HashMap;

/**
 * This is a generic project type that has no compiler defined
 */
public class GenericProjectType implements CoderProjectType {
    private HashMap<String,GenericProject> projects = projects = new HashMap<>();


    @Override
    public void init() { }

    @Override
    public String getName() {
        return "Generic";
    }

    @Override
    public SupportedProperties getSupportedConfiguration() {
        return new SupportedProperties();
    }

    @Override
    public Project createProject(String name) {
        projects.put(name, new GenericProject(name, this));
        return projects.get(name);
    }

    @Override
    public Project getProject(String name) {
        return projects.get(name);
    }
}
