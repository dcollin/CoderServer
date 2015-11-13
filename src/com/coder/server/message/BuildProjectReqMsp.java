package com.coder.server.message;

/**
 * Created by Ziver on 2015-11-13.
 */
public class BuildProjectReqMsp {
    public static enum CompileStatus{
        /** Build was successful **/
        SUCCESS,
        /** Build was successful but with warnings **/
        WARNING,
        /** Build failed **/
        FAILED
    }

    public CompileStatus result;
}
