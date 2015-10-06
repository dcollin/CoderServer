package com.coder.server.plugin.arduino;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import com.coder.server.plugin.CoderCompiler.CompileStatus;
import com.coder.server.plugin.ExecListener;
import zutil.log.LogUtil;

import com.coder.server.plugin.ExecInstance;
import com.coder.server.struct.Project;
import com.coder.server.util.OSAbstractionLayer;
import com.coder.server.util.ExtendedProperties;

public class ArduinoCompiler {
	private static final Logger logger = LogUtil.getLogger();

	private HashMap<File, Long> lastModificationTimeMap;
	private ExtendedProperties compileProperties;

	
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
	public CompileStatus compile(Project proj, ExecListener listener) {
		if(listener != null){
			listener.execOutput("VERIFYING SKETCH...");
		}
		
		if( (proj instanceof ArduinoProject) == false){
			return CompileStatus.COMPILE_FAILED;
		}
		ArduinoProject arduinoProject = (ArduinoProject)proj;
		ExtendedProperties buildProperies = arduinoProject.getBuildProperties();
		
		//convert sketch file .ino -> .cpp
		//TODO
		
		//build sketch file
		File sketchFile = new File(buildProperies.resolveString("{build_path}/{build.project_name}"));
		CompileStatus buildStatus = compileFile(sketchFile, buildProperies, listener);
		if(buildStatus != CompileStatus.COMPILE_SUCCESS){
			return buildStatus;
		}
		
		//build libraries
		//TODO
		
		//build archive
		String archiveDirPath = buildProperies.resolveString("{runtime.platform.path}/hardware/{board.vendor}/{board.architecture}/cores/{build.core}/");
		buildProperies.setProperty("archive_file", "core.a");
		buildStatus = buildArchive(archiveDirPath, buildProperies, listener);
		if(buildStatus == CompileStatus.COMPILE_FAILED){
			return buildStatus;
		}else if(buildStatus == CompileStatus.FILE_NOT_EXISTS){
			return buildStatus;
		}
		
		//link
		if(executeCommand(buildProperies.getProperty("recipe.c.combine.pattern"), listener) != 0){
			return buildStatus;
		}
		if(executeCommand(buildProperies.getProperty("recipe.objcopy.eep.pattern"), listener) != 0){
			return buildStatus;
		}
		if(executeCommand(buildProperies.getProperty("recipe.objcopy.hex.pattern"), listener) != 0){
			return buildStatus;
		}
		
		//calculate size of built file
		//TODO
		
		if(listener != null){
			listener.execOutput("VERIFYING DONE");
		}
		return CompileStatus.COMPILE_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.coder.server.plugin.CoderCompiler#run(com.coder.server.struct.Project, java.io.Writer, java.io.Reader)
	 */
	public ExecInstance createExecInstance(ArduinoProject proj) {
		String port = proj.getTargetPort();
		int baudrate = proj.getTargetBaudrate();
		ArduinoExecInstance execInstance = new ArduinoExecInstance(this, port, baudrate);
		return execInstance;
	}
	
	private CompileStatus buildArchive(String archiveDirPath, ExtendedProperties buildProperies, ExecListener listener){
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
	
	private CompileStatus compileFile(File sourceFile, ExtendedProperties buildProperies, ExecListener listener){
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
	
	private int executeCommand(String cmd, ExecListener listener){
		int exitCode = 0;
		exitCode = OSAbstractionLayer.executeCommand(cmd, null, null);
		return exitCode;
	}

}
