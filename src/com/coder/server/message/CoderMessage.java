package com.coder.server.message;

import java.util.HashMap;


public class CoderMessage {

    // Authentication messaging
    public AuthenticationReqMsg            AuthenticationReq;
    public AuthenticationChallengeMsg      AuthenticationChallenge;
    public AuthenticationRspMsg            AuthenticationRsp;
    public AuthenticationSuccessMsg        AuthenticationSuccess;


    // Project messaging
    public ProjectTypeReqMsg               ProjectTypeReq;
    public ProjectTypeRspMsg               ProjectTypeRsp;

    public ProjectListReqMsg               ProjectListReq;
    public ProjectListRspMsg               ProjectListRsp;

    public ProjectCreateReqMsg             ProjectCreateReq;
    public ProjectReqMsg                   ProjectReq;
    public ProjectRspMsg                   ProjectRsp;


    // File messaging
    public Object FileCreateReq;
    public Object FileCreateRsp;

    public Object FileReq;
    public Object FileRsp;

    public Object FileSaveReq;
    public Object FileSaveRsp;


    // Build and run messaging

}
