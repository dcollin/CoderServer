package com.coder.server.plugin;

import com.coder.server.Project;

public interface CoderCompiler {
	public void init();
	public boolean compile(Project proj);
	public void run(Project proj);
}
