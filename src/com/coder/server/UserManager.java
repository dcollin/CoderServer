package com.coder.server;

import com.coder.server.struct.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
        users.put(user.getUsername(), user);
    }


    protected static void initialize() throws IOException {
        instance = new UserManager();

        List<Properties> users = ConfigManager.getInstance().getUserConfs();
        for(Properties userProp : users){
            instance.addUser(new User(userProp));
        }
    }

    public static UserManager getInstance(){
        return instance;
    }
}
