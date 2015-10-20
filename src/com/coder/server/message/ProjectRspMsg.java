package com.coder.server.message;

import java.util.List;


public class ProjectRspMsg {

    public String name;
    public String type;
    public String description;
    public ConfigData config;
    public List<String> fileList;

    /** OPTIONAL **/
    public String error;
}
