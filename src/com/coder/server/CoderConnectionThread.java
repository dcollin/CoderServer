package com.coder.server;

import zutil.Encrypter;
import zutil.log.LogUtil;
import zutil.net.threaded.ThreadedTCPNetworkServerThread;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ziver on 2015-09-29.
 */
public class CoderConnectionThread implements ThreadedTCPNetworkServerThread {
    private static final Logger logger = LogUtil.getLogger();

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public CoderConnectionThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String key = authenticate();
            Encrypter crypto = new Encrypter(key, Encrypter.AES_ALGO);
            this.in = new BufferedReader(new InputStreamReader(
                    crypto.decrypt(socket.getInputStream())));
            this.out = new BufferedWriter(new OutputStreamWriter(
                    crypto.encrypt(socket.getOutputStream())));

            // TODO: start parsing incomming messages from client
        }catch (Exception e){
            logger.log(Level.SEVERE, "Client Connection issue", e);
            try {
                socket.close();
                socket = null;
            } catch (IOException e1) {
                logger.log(Level.SEVERE, null, e1);
            }
        }
    }

    private String authenticate(){
        // TODO: get username and generate AES key
        return "";
    }
}
