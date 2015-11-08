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
    public FileCreateReqMsg                FileCreateReq;
    public FileReqMsg FileReq;
    public FileRspMsg FileRsp;


    // Build and run messaging

}
