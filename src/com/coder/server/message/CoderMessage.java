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

    public ProjectConfigureReqMsg          ProjectConfigureReq; //TODO
    public ProjectConfigureRspMsg          ProjectConfigureRsp; //TODO


    // File messaging
    public FileCreateReqMsg                FileCreateReq;
    public FileReqMsg                      FileReq;
    public FileRspMsg                      FileRsp;

    public FileSaveReqMsg                  FileSaveReq; //TODO
    public FileSaveRspMsg                  FileSaveRsp; //TODO

    public FileDeleteReqMsg                FileDeleteReq; //TODO
    public FileDeleteRspMsg                FileDeleteRsp; //TODO


    // Build and run messaging
    public BuildProjectReqMsg              BuildProjectReq; //TODO
    public BuildProjectReqMsp              BuildProjectRsp; //TODO

    public RunProjectReqMsg                RunProjectReq;   //TODO
    public TerminateRunReqMsg              TerminateRunReq; //TODO
    public RunProjectReqMsp                RunProjectRsp;   //TODO

    public TerminalInputReqMsg             TerminalInputReq;  //TODO
    public TerminalInputRspMsg             TerminalInputRsp;  //TODO
    public TerminalOutputIndMsg            TerminalOutputInd; //TODO
}
