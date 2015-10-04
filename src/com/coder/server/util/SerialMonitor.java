package com.coder.server.util;

import com.coder.server.plugin.ExecInstance;
import com.coder.server.plugin.ExecListener;
import com.fazecast.jSerialComm.SerialPort;
import zutil.log.LogUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ziver on 2015-10-04.
 */
public class SerialMonitor extends Thread implements ExecInstance{
    private static final Logger logger = LogUtil.getLogger();

    private SerialPort serial;
    private BufferedReader in;
    private BufferedWriter out;
    private ExecListener listener;


    public SerialMonitor(String portName, int baudrate) throws IOException {
        logger.info("Opening serial port: "+ portName +" baud: "+baudrate);
        serial = SerialPort.getCommPort(portName);
        serial.setBaudRate(baudrate);
        if(!serial.openPort())
            throw new IOException("Could not open port: "+portName);
        //serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 5000, 0); // Timeout after 5sec
        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        in  = new BufferedReader(new InputStreamReader(serial.getInputStream(),  "UTF-8"));
        out = new BufferedWriter(new OutputStreamWriter(serial.getOutputStream(), "UTF-8"));
    }

    @Override
    public void send(String line) {
        try {
            out.write(line);
            out.flush();
        }catch (IOException e){
            logger.log(Level.SEVERE, "Unable to send data to serial port", e);
        }
    }

    public void run(){
        try {
            while (serial != null) {
                String data = in.readLine();
                if(listener != null)
                    listener.execOutput(data);
            }
        }catch (IOException e){
            logger.log(Level.SEVERE, "Unable to read from serial port", e);
        }
        close();
    }

    public void close(){
        if(serial != null) {
            serial.closePort();
            serial = null;
        }
    }

    public static List<String> getAvailablePorts(){
        List<String> list = new ArrayList<>();
        for(SerialPort port : SerialPort.getCommPorts()){
            list.add(port.getSystemPortName());
        }
        return list;
    }



    @Override
    public void setExecListener(ExecListener listener) {
        this.listener = listener;
    }

    @Override
    public void exec() {
        super.start();
    }

    @Override
    public boolean isRunning() {
        return serial != null;
    }
}
