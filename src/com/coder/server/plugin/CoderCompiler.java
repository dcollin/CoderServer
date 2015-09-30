package com.coder.server.plugin;

import java.io.Reader;
import java.io.Writer;

import com.coder.server.struct.Project;

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
