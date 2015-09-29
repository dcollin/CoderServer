package com.coder.server.plugin;

import com.coder.server.struct.Project;

public interface CoderCompiler {
	public String getName();
	public void init();
	public boolean compile(Project proj);
	public void run(Project proj);
}
