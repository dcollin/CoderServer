package com.coder.server.message;

import java.util.ArrayList;
import java.util.Properties;


public class ProjectRspMsg {

    public String name;
    public String type;
    public String description;
    public Properties config;
    public ArrayList<String> fileList;

    /** OPTIONAL **/
    public String error;
}
