package com.coder.server.plugin;

public interface CoderCompiler {
    public static enum CompileStatus{
        COMPILE_SUCCESS,	    //the file was compiled
        COMPILE_SKIPPED,	    //the file has not been changed and has already been compiled earlier
        COMPILE_FAILED,		    //the compilation failed
        FILE_NOT_SOURCE_FILE,	//the file is skipped since it is not a source file
        FILE_NOT_EXISTS		    //file missing
    }
		
	/**
	 * Compile the project (blocking)
	 * @return true if the compilation went well, else false
	 */
	public CompileStatus compile(ExecListener listener);
	
	/**
	 * (non-blocking)
     */
	public ExecInstance createExecInstance();
}
