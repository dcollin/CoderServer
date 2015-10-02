package com.coder.server.plugin.arduino;

import com.coder.server.plugin.ExecInstance;
import com.coder.server.plugin.ExecListener;

public class ArduinoExecInstance extends ExecInstance {
	
	private ArduinoCompiler compiler;
	private String targetPort;

	public ArduinoExecInstance(ArduinoCompiler compiler, String targetPort) {
		this.compiler = compiler;
		this.targetPort = targetPort;
	}

	@Override
	public void exec() {
		ExecListener listener = getExecListener();
		listener.execOutput("UPLOADING SKETCH TO BOARD...");
		//TODO
		listener.execOutput("DONE UPLOADING");
		//TODO
		listener.execOutput("OPENING SERIAL PORT ("+targetPort+")");
		//TODO
		listener.execOutput("SERIAL COMMUNICATION SETUP DONE");
		//TODO
	}

	@Override
	public void send(String line) {
		ExecListener listener = getExecListener();
		listener.execOutput("DEBUG: sending line over serial: \""+line+"\"");
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

}
