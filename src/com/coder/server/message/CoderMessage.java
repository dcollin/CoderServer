package com.coder.server.message;


import java.util.List;
import java.util.Map;

public class CoderMessage {

    // Authentication messaging
    public AuthenticationReqMsg         AuthenticationReq;
    public AuthenticationChallengeMsg   AuthenticationChallenge;
    public AuthenticationRspMsg         AuthenticationRsp;
    public AuthenticationSuccessMsg     AuthenticationSuccess;


    // Project messaging
    public ProjectTypeReqMsg            ProjectTypeReq;
    public Map<String,ConfigData>       ProjectTypeRsp;

    public ProjectListReqMsg            ProjectListReq;
    public Map<String,ProjectListData>  ProjectListRsp;

    public ProjectCreateReqMsg          ProjectCreateReq;
    public ProjectReqMsg                ProjectReq;
    public ProjectRspMsg                ProjectRsp;


    // File messaging
    public Object FileCreateReq;
    public Object FileCreateRsp;

    public Object FileReq;
    public Object FileRsp;

    public Object FileSaveReq;
    public Object FileSaveRsp;


    // Build and run messaging

    // Other messages
    public Object PortInfoReq;
    public Object PortInfoRsp;
}
