package com.coder.server.plugin.generic;

import com.coder.server.message.ConfigData;
import com.coder.server.plugin.CoderCompiler;
import com.coder.server.plugin.CoderProjectType;
import com.coder.server.struct.Project;

/**
 * Created by ezivkoc on 2015-10-21.
 */
public class GenericProject extends Project {

    public GenericProject(String name, CoderProjectType projectType) {
        super(name, projectType);
    }

    @Override
    public ConfigData getConfiguration() {
        return null;
    }

    @Override
    public CoderCompiler getCompiler() {
        return null;
    }

    @Override
    public void setConfiguration(ConfigData data) {

    }
}
