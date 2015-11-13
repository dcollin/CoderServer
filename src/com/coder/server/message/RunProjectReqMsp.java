package com.coder.server.message;

/**
 * Created by Ziver on 2015-11-13.
 */
public class RunProjectReqMsp {
    public static enum ExitCode {
        /** The Run exited with no issues **/
        SUCCESS,
        /** The Run exited with an exit code failed **/
        FAILED,
        /** The run was terminated by user of application **/
        TERMINATED
    }

    public ExitCode exit_code;
}
