package com.coder.server.struct;

/**
 * Created by Ziver on 2015-09-29.
 */
public class User {
    private String username;
    private String passwordHash;


    public User(String username){
        this.username = username;
    }


    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
}
