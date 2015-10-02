package com.coder.server.plugin;

/**
 * Created by Ziver on 2015-09-30.
 */
public abstract class ExecInstance {

	private ExecListener listener = null;
	
    public void setExecListener(ExecListener listener){
    	this.listener = listener;
    }
    
    public ExecListener getExecListener(){
    	return this.listener;
    }

    public abstract void exec();

    public abstract void send(String line);

    public abstract boolean isRunning();

}
