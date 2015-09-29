package com.coder.server.plugin.arduino;

import com.coder.server.struct.Project;
import com.coder.server.plugin.CoderCompiler;
import com.coder.server.util.CommandExecutor;
import com.coder.server.util.ExtendedProperties;

public class ArduinoCompiler implements CoderCompiler {

	HashMap<File, Long> lastModificationTimeMap = new HashMap<File, Long>();
	ExtendedProperties compileProperties = new ExtendedProperties();
	
	private enum CompileStatus{
		COMPILE_SUCCESS,	//the file was compiled
		COMPILE_SKIPPED,	//the file has not been changed and has already been compiled earlier
		COMPILE_FAILED,		//the compilation failed
		FILE_NOT_SOURCE_FILE,	//the file is skipped since it is not a source file
		FILE_NOT_EXISTS		//file missing
	}
	
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
	
	private CompileStatus buildArchive(ExtendedProperties buildProperies){
		String archiveDirPath = buildProperies.resolveString("{runtime.platform.path}/hardware/{board.vendor}/{board.architecture}/cores/{build.core}/");
		File archiveSourceFolder = new File(archiveDirPath);
		
		//check if the folder exists
		if(archiveSourceFolder == null || !archiveSourceFolder.exists() || !archiveSourceFolder.isDirectory()){
			return CompileStatus.FILE_NOT_EXISTS;
		}
		
		//compile all source files in the 
		boolean fileRecompiled = false;
		for(File sourceFile : archiveSourceFolder.listFiles()){
			if(sourceFile.isFile()){
				CompileStatus status = compileFile(sourceFile, buildProperies);
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
					int exitCode = CommandExecutor.executeCommand(buildProperies.getProperty("recipe.ar.pattern"));
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
	
	private CompileStatus compileFile(File sourceFile, ExtendedProperties buildProperies){
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
			exitCode = CommandExecutor.executeCommand(buildProperies.getProperty("recipe.S.o.pattern"));
		}else if(sourceFile.getName().endsWith(".c")){
			exitCode = CommandExecutor.executeCommand(buildProperies.getProperty("recipe.c.o.pattern"));
		}else if(sourceFile.getName().endsWith(".cpp")){
			exitCode = CommandExecutor.executeCommand(buildProperies.getProperty("recipe.cpp.o.pattern"));
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

}
