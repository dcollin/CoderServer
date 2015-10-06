package com.coder.server.plugin;

/**
 * Created by Ziver on 2015-09-30.
 */
public interface ExecInstance {

    public void setExecListener(ExecListener listener);


    public abstract void exec();

    public abstract void send(String line);

    public abstract boolean isRunning();

    public abstract void terminate();
    
}
