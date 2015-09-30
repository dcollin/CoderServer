package com.coder.server.plugin;

/**
 * Created by Ziver on 2015-09-30.
 */
public interface CompileStatusListener{
    public static enum CompileStatus{
        SUCCESSFUL, SUCCESSFULL_WITH_WARNINGS, FAILED
    }

    public void compileLog(String line);

    public void compilationDone(CompileStatus status);
}
