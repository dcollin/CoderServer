package com.coder.server.struct;

import zutil.Hasher;

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
