package com.coder.server;

import com.coder.server.struct.User;

import java.util.HashMap;

/**
 * This class handles a record of all user.
 */
public class UserManager {
    private static UserManager instance;

    /** The actual user DB **/
    private HashMap<String, User> users = new HashMap<>();


    private UserManager(){ }


    public User getUser(String username){
        return users.get(username);
    }
    public void addUser(User user){

    }


    protected static void initialize(){
        instance = new UserManager();
    }

    public static UserManager getInstance(){
        return instance;
    }
}
