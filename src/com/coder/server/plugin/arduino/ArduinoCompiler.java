package com.coder.server.plugin.arduino;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;

import com.coder.server.struct.Project;
import com.coder.server.plugin.CoderCompiler;
import com.coder.server.util.OSAbstractionLayer;
import com.coder.server.util.ExtendedProperties;

public class ArduinoCompiler implements CoderCompiler {

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
	@Override
	public boolean compile(Project proj, Writer out) {
		PrintWriter printOut = new PrintWriter(out);
		printOut.println("VERIFYING SKETCH...");
		
		if( (proj instanceof ArduinoProject) == false){
			return false; 
		}
		ArduinoProject arduinoProject = (ArduinoProject)proj;
		ExtendedProperties buildProperies = arduinoProject.getBuildProperties();
		
		//convert sketch file .ino -> .cpp
		//TODO
		
		//build sketch file
		File sketchFile = new File(buildProperies.resolveString("{build_path}/{build.project_name}"));
		CompileStatus buildStatus = compileFile(sketchFile, buildProperies, out);
		if(buildStatus != CompileStatus.COMPILE_SUCCESS){
			return false;
		}
		
		//build libraries
		//TODO
		
		//build archive
		String archiveDirPath = buildProperies.resolveString("{runtime.platform.path}/hardware/{board.vendor}/{board.architecture}/cores/{build.core}/");
		buildProperies.setProperty("archive_file", "core.a");
		buildStatus = buildArchive(archiveDirPath, buildProperies, out);
		if(buildStatus == CompileStatus.COMPILE_FAILED){
			return false;
		}else if(buildStatus == CompileStatus.FILE_NOT_EXISTS){
			return false;
		}
		
		//link
		if(OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.c.combine.pattern"), out, null) != 0){
			return false;
		}
		if(OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.objcopy.eep.pattern"), out, null) != 0){
			return false;
		}
		if(OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.objcopy.hex.pattern"), out, null) != 0){
			return false;
		}
		
		//calculate size of built file
		//TODO
		
		printOut.println("VERIFYING DONE");
		return true;
	}

	/* (non-Javadoc)
	 * @see com.coder.server.plugin.CoderCompiler#run(com.coder.server.struct.Project, java.io.Writer, java.io.Reader)
	 */
	@Override
	public void run(Project proj, Writer out, Reader in) {
		PrintWriter printOut = new PrintWriter(out);
		
		
	}
	
	private CompileStatus buildArchive(String archiveDirPath, ExtendedProperties buildProperies, Writer out){
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
				CompileStatus status = compileFile(sourceFile, buildProperies, out);
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
					int exitCode = OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.ar.pattern"), out, null);
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
	
	private CompileStatus compileFile(File sourceFile, ExtendedProperties buildProperies, Writer out){
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
			exitCode = OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.S.o.pattern"), out, null);
		}else if(sourceFile.getName().endsWith(".c")){
			exitCode = OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.c.o.pattern"), out, null);
		}else if(sourceFile.getName().endsWith(".cpp")){
			exitCode = OSAbstractionLayer.executeCommand(buildProperies.getProperty("recipe.cpp.o.pattern"), out, null);
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
