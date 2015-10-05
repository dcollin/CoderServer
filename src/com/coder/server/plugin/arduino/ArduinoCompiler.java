package com.coder.server.plugin.arduino;

import java.io.File;
import java.util.HashMap;

import com.coder.server.plugin.CompileStatusListener;
import com.coder.server.plugin.ExecInstance;
import com.coder.server.struct.Project;
import com.coder.server.util.OSAbstractionLayer;
import com.coder.server.util.ExtendedProperties;
import com.fazecast.jSerialComm.SerialPort;

public class ArduinoCompiler {

	private HashMap<File, Long> lastModificationTimeMap;
	private ExtendedProperties compileProperties;


	private enum CompileStatus{
		COMPILE_SUCCESS,	//the file was compiled
		COMPILE_SKIPPED,	//the file has not been changed and has already been compiled earlier
		COMPILE_FAILED,		//the compilation failed
		FILE_NOT_SOURCE_FILE,	//the file is skipped since it is not a source file
		FILE_NOT_EXISTS		//file missing
	}
	
	public ArduinoCompiler(){
		lastModificationTimeMap = new HashMap<File, Long>();
		compileProperties = new ExtendedProperties();
	}
	
	public ExtendedProperties getProperties(){
		return this.compileProperties;
	}
	
	/* (non-Javadoc)
	 * @see com.coder.server.plugin.CoderCompiler#compile(com.coder.server.struct.Project, java.io.Writer)
	 */
	public boolean compile(Project proj, CompileStatusListener listener) {
		if(listener != null){
			listener.compileLog("VERIFYING SKETCH...");
		}
		
		if( (proj instanceof ArduinoProject) == false){
			return false; 
		}
		ArduinoProject arduinoProject = (ArduinoProject)proj;
		ExtendedProperties buildProperies = arduinoProject.getBuildProperties();
		
		//convert sketch file .ino -> .cpp
		//TODO
		
		//build sketch file
		File sketchFile = new File(buildProperies.resolveString("{build_path}/{build.project_name}"));
		CompileStatus buildStatus = compileFile(sketchFile, buildProperies, listener);
		if(buildStatus != CompileStatus.COMPILE_SUCCESS){
			return false;
		}
		
		//build libraries
		//TODO
		
		//build archive
		String archiveDirPath = buildProperies.resolveString("{runtime.platform.path}/hardware/{board.vendor}/{board.architecture}/cores/{build.core}/");
		buildProperies.setProperty("archive_file", "core.a");
		buildStatus = buildArchive(archiveDirPath, buildProperies, listener);
		if(buildStatus == CompileStatus.COMPILE_FAILED){
			return false;
		}else if(buildStatus == CompileStatus.FILE_NOT_EXISTS){
			return false;
		}
		
		//link
		if(executeCommand(buildProperies.getProperty("recipe.c.combine.pattern"), listener) != 0){
			return false;
		}
		if(executeCommand(buildProperies.getProperty("recipe.objcopy.eep.pattern"), listener) != 0){
			return false;
		}
		if(executeCommand(buildProperies.getProperty("recipe.objcopy.hex.pattern"), listener) != 0){
			return false;
		}
		
		//calculate size of built file
		//TODO
		
		if(listener != null){
			listener.compileLog("VERIFYING DONE");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.coder.server.plugin.CoderCompiler#run(com.coder.server.struct.Project, java.io.Writer, java.io.Reader)
	 */
	public ExecInstance createExecInstance(ArduinoProject proj) {
		SerialPort port = proj.getTargetPort();
		ArduinoExecInstance execInstance = new ArduinoExecInstance(this, port);
		return execInstance;
	}
	
	private CompileStatus buildArchive(String archiveDirPath, ExtendedProperties buildProperies, CompileStatusListener listener){
		//get the build properties from the project
		
		File archiveSourceFolder = new File(archiveDirPath);
		
		//check if the folder exists
		if(archiveSourceFolder == null || !archiveSourceFolder.exists() || !archiveSourceFolder.isDirectory()){
			return CompileStatus.FILE_NOT_EXISTS;
		}
		
		//compile all source files in the 
		boolean fileRecompiled = false;
		for(File sourceFile : archiveSourceFolder.listFiles()){
			if(sourceFile.isFile()){
				CompileStatus status = compileFile(sourceFile, buildProperies, listener);
				switch(status){
					case FILE_NOT_EXISTS:
					case COMPILE_FAILED:
						return CompileStatus.COMPILE_FAILED;
					case COMPILE_SUCCESS:
						fileRecompiled = true;
						break;
					default:
						break;
				}
			}
		}
		
		//need to recompile the archive?
		if(fileRecompiled){
			for(File sourceFile : archiveSourceFolder.listFiles()){
				if(sourceFile.isFile()){
					buildProperies.setProperty("object_file", "{build.path}/"+sourceFile.getName()+".o");
					int exitCode = executeCommand(buildProperies.getProperty("recipe.ar.pattern"), listener);
					if(exitCode != 0){
						return CompileStatus.COMPILE_FAILED;
					}
				}
			}
		}else{
			System.out.println("INFO: skipping compiling \""+buildProperies.getProperty("{archive_file}")+"\" due to no source files have been modified since last compilation.");
		}
		
		return CompileStatus.COMPILE_SUCCESS;
	}
	
	private CompileStatus compileFile(File sourceFile, ExtendedProperties buildProperies, CompileStatusListener listener){
		buildProperies.setProperty("source_file", sourceFile.getAbsolutePath());
		buildProperies.setProperty("object_file", "{build.path}/"+sourceFile.getName()+".o");
		
		//check if the file exists
		if(sourceFile == null || !sourceFile.exists() || !sourceFile.isFile()){
			return CompileStatus.FILE_NOT_EXISTS;
		}
		
		//file already compiled and not modified since?
		Long lastModDate = lastModificationTimeMap.get(sourceFile);
		File objectFile = new File(buildProperies.getProperty("object_file"));
		if(lastModDate != null && lastModDate == sourceFile.lastModified() && objectFile.exists()){
			return CompileStatus.COMPILE_SKIPPED;
		}
		
		//compile the source file
		int exitCode = 0;
		if(sourceFile.getName().endsWith(".S")){
			exitCode = executeCommand(buildProperies.getProperty("recipe.S.o.pattern"), listener);
		}else if(sourceFile.getName().endsWith(".c")){
			exitCode = executeCommand(buildProperies.getProperty("recipe.c.o.pattern"), listener);
		}else if(sourceFile.getName().endsWith(".cpp")){
			exitCode = executeCommand(buildProperies.getProperty("recipe.cpp.o.pattern"), listener);
		}else{
			return CompileStatus.FILE_NOT_SOURCE_FILE;
		}
		if(exitCode != 0){
			return CompileStatus.COMPILE_FAILED;
		}
		
		//save the timestamp of the last modification of the source file
		lastModificationTimeMap.put(sourceFile, sourceFile.lastModified());
		return CompileStatus.COMPILE_SUCCESS;
	}
	
	private int executeCommand(String cmd, CompileStatusListener listener){
		int exitCode = 0;
		exitCode = OSAbstractionLayer.executeCommand(cmd, null, null);
		return exitCode;
	}

}
