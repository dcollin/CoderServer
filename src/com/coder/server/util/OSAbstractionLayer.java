package com.coder.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public abstract class OSAbstractionLayer {

    public static int executeCommand(String cmd, Writer out){
        return executeCommand(cmd, out, null);
    }
    /**
     * @param cmd The command to run
     * @param out The output from the executing command will be written here
     * @param in The input sent to the executing command will be read here
     * @return
     */
    public static int executeCommand(String cmd, Writer out, Reader in){
    	int exitCode = 0;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmd);

            BufferedReader procOutput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedWriter procInput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            boolean dataAvailable = true;
            while(dataAvailable){
                dataAvailable = false;
            	while(out != null && procOutput.ready()) {
                    out.write(procOutput.read());
                    dataAvailable = true;
                }
            	while(in != null && in.ready()) {
                    procInput.write(in.read());
                    dataAvailable = true;
                }
            }
            
            exitCode = proc.waitFor();

            procOutput.close();
            procInput.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exitCode;
    }

}