package com.coder.server.struct;

import zutil.Hasher;

import java.util.Properties;

/**
 * Created by Ziver
 */
public class User {
    public static final String USER_USERNAME_PROPERTY = "username";
    public static final String USER_PASSHASH_PROPERTY = "passhash";
    public static final int PASSWORD_HASH_ITERATIONS = 500;

    private String username;
    private String passwordHash;


    public User(String username){
        this.username = username;
    }
    public User(Properties userProp){
        setProperties(userProp);
    }

    public void setProperties(Properties userProp) {
        this.username = userProp.getProperty(USER_USERNAME_PROPERTY);
        this.passwordHash = userProp.getProperty(USER_PASSHASH_PROPERTY);
    }
    public Properties getProperties(){
        Properties userProp = new Properties();
        userProp.setProperty(USER_USERNAME_PROPERTY, username);
        userProp.getProperty(USER_PASSHASH_PROPERTY, passwordHash);
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
