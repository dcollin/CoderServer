package com.coder.server.plugin.arduino;

import com.coder.server.struct.Project;
import com.coder.server.plugin.CoderCompiler;

public class ArduinoCompiler implements CoderCompiler {

	@Override
	public String getName() {
		return "Arduino";
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public boolean compile(Project proj) {
		if( (proj instanceof ArduinoProject) == false){
			return false; 
		}
		ArduinoProject arduinoProject = (ArduinoProject)proj;
		
		return true;
	}

	@Override
	public void run(Project proj) {
		
	}

}
