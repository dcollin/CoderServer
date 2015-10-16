package com.coder.server.struct;

import zutil.Hasher;

import java.util.Properties;

/**
 * Created by Ziver on 2015-09-29.
 */
public class User {
    public static final int PASSWORD_HASH_ITERATIONS = 500;

    private String username;
    private String passwordHash;


    public User(String username){
        this.username = username;
    }

    public User(Properties userProp) {
        this.username = userProp.getProperty("username");
        this.passwordHash = userProp.getProperty("passhash");
    }
    public Properties getProperties(){
        Properties userProp = new Properties();
        userProp.setProperty("username", username);
        userProp.getProperty("passhash", passwordHash);
        return userProp;
    }

    public void setPassword(String password){
        this.passwordHash =
                Hasher.PBKDF2(password, username, PASSWORD_HASH_ITERATIONS);
    }

    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }


}
