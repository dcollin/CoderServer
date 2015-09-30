package com.coder.server.plugin;

import java.io.Reader;
import java.io.Writer;

import com.coder.server.struct.Project;

public interface CoderCompiler {
	
	public void init();
	
	/**
	 * Compile the project
	 * @param proj The project to compile
	 * @param out The output from the compilation
	 * @return true if the compilation went well, else false
	 */
	public boolean compile(Project proj, Writer out);
	
	/**
	 * Run the compiled code.
	 * @param proj The project to run
	 * @param out The output from the running binary
	 * @param in The input to the running binary
	 */
	public void run(Project proj, Writer out, Reader in);
}
