package com.coder.server.message;

/**
 * Created by Ziver on 2015-11-08.
 */
public class FileRspMsg {
    public String path;
    public byte[] data;

    /* OPTIONAL */
    public String error;
}
