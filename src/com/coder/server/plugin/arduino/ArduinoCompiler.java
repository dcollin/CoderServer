package com.coder.server.plugin.arduino;

import java.io.File;
import java.util.HashMap;

import com.coder.server.Project;
import com.coder.server.plugin.CoderCompiler;

public class ArduinoCompiler implements CoderCompiler {

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
