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
    public int FileCreateReq;
    public int FileCreateRsp;

    public int FileReq;
    public int FileRsp;

    public int FileSaveReq;
    public int FileSaveRsp;


    // Build and run messaging

    // Other messages
    public int PortInfoReq;
    public int PortInfoRsp;
}
