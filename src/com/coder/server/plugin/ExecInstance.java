package com.coder.server.plugin;

/**
 * Created by Ziver on 2015-09-30.
 */
public interface ExecInstance {

    public void setExecListener(ExecListener listener);

    public void exec();

    public void send(String line);

    public boolean isRunning();

}
