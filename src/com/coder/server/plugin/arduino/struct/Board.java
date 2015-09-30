package com.coder.server.plugin.arduino.struct;

import com.coder.server.util.ExtendedProperties;

public class Board {

	private ExtendedProperties boardProperties = new ExtendedProperties();
	
	public Board(){
		
	}
	
	public ExtendedProperties getProperties(){
		return this.boardProperties;
	}
	
}
