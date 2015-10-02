package com.coder.server.plugin;

public interface CoderCompiler {
		
	/**
	 * Compile the project (blocking)
	 * @return true if the compilation went well, else false
	 */
	public boolean compile(CompileStatusListener listener);
	
	/**
	 * (non-blocking)
     */
	public ExecInstance createExecInstance();
}
