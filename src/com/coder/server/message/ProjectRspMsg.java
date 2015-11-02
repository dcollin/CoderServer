package com.coder.server.message;

import java.util.List;
import java.util.Properties;


public class ProjectRspMsg {

    public String name;
    public String type;
    public String description;
    public Properties config;
    public List<String> fileList;

    /** OPTIONAL **/
    public String error;
}
