package com.coder.server.message;


public class CoderMessage {

    // Authentication messaging
    public AuthenticationReqMsg AuthenticationReq;
    public AuthenticationChallengeMsg AuthenticationChallenge;
    public AuthenticationRspMsg AuthenticationRsp;
    public AuthenticationSuccessMsg AuthenticationSuccess;


    // Project messaging
    public int ProjectTypeReq;
    public int ProjectTypeRsp;

    public int ProjectCreateReq;
    public int ProjectCreateRsp;

    public int ProjectListReq;
    public int ProjectListRsp;

    public int ProjectReq;
    public int ProjectRsp;


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
