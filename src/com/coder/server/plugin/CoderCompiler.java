package com.coder.server.plugin;

import com.coder.server.message.BuildProjectReqMsp;

public interface CoderCompiler {

	/**
	 * Compile the project (blocking)
	 * @return true if the compilation went well, else false
	 */
	public BuildProjectReqMsp.CompileStatus compile(ExecListener listener);
	
	/**
	 * (non-blocking)
     */
	public ExecInstance createExecInstance();
}
