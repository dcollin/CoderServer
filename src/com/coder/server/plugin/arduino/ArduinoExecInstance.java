package com.coder.server.plugin.arduino;

import java.io.IOException;
import java.util.logging.Logger;

import zutil.log.LogUtil;

import com.coder.server.plugin.ExecInstance;
import com.coder.server.plugin.ExecListener;
import com.coder.server.util.SerialMonitor;

public class ArduinoExecInstance implements ExecInstance {
	private static final Logger logger = LogUtil.getLogger();
	
	private ArduinoCompiler compiler;
	private ExecListener listener;
	private String targetPort;
	private int targetBaudeRate;
	private SerialMonitor serial;

	public ArduinoExecInstance(ArduinoCompiler compiler, String targetPort, int targetBaudeRate) {
		this.compiler = compiler;
		this.targetPort = targetPort;
	}

	@Override
	public void setExecListener(ExecListener listener) {
		this.listener = listener;
	}

	@Override
	public void exec() {
		if(listener != null) {
			listener.execOutput("UPLOADING SKETCH TO BOARD...");
			//TODO
			listener.execOutput("DONE UPLOADING");
			//TODO
			
			listener.execOutput("OPENING SERIAL PORT (" + targetPort + ")");
			try {
				serial = new SerialMonitor(targetPort, targetBaudeRate);
			} catch (IOException e) {
				listener.execOutput(e.getMessage());
				return;
			}
			serial.setExecListener(listener);
			listener.execOutput("SERIAL COMMUNICATION SETUP DONE, STARTING COMMUNICATION");
			serial.exec();
		}
	}

	@Override
	public void send(String line) {
		if(listener != null && serial != null){
			serial.send(line);
		}
	}

	@Override
	public boolean isRunning() {
		if(serial != null && serial.isRunning()){
			return true;
		}
		return false;
	}
	
	@Override
	public void terminate(){
		if(isRunning()){
			serial.terminate();
		}
	}

}
