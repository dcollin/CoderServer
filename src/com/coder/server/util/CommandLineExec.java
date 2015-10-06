package com.coder.server.util;

import com.coder.server.plugin.ExecInstance;
import com.coder.server.plugin.ExecListener;
import zutil.log.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineExec extends Thread implements ExecInstance {
    private static final Logger logger = LogUtil.getLogger();

    private String cmd;
    private Process proc;
    private BufferedReader in;
    private BufferedWriter out;
    private ExecListener listener;
    private int exitCode = -1;


    public CommandLineExec(String cmd){
        this.cmd = cmd;
    }


    public void run(){
        try {
            proc = Runtime.getRuntime().exec(cmd);

            in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            String output;
            while((output=in.readLine()) != null){
                if(listener != null)
                    listener.execOutput(output);
            }
            
            exitCode = proc.waitFor();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        } finally {
            terminate();
        }
    }

    @Override
    public void setExecListener(ExecListener listener) {
        this.listener = listener;
    }

    @Override
    public synchronized void exec() {
        if(proc == null)
            start();
    }

    @Override
    public void send(String line) {
        if(out != null)
            try {
                out.write(line);
            } catch (IOException e) {
                logger.log(Level.WARNING, null, e);
            }
    }

    @Override
    public boolean isRunning() {
        return proc != null && exitCode < 0;
    }

    @Override
    public void terminate() {
        try {
            if (proc != null)
                proc.destroy();
            if (out != null)
                out.close();
            if (in != null)
                in.close();
        } catch(Exception e){
            logger.log(Level.SEVERE, null, e);
        }
        out = null;
        in = null;
    }
}