package com.coder.server;

import com.coder.server.struct.User;
import zutil.log.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class handles a record of all user.
 */
public class UserManager {
    private static final Logger logger = LogUtil.getLogger();
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
            logger.info("Found user: "+userProp.getProperty(User.USER_USERNAME_PROPERTY));
            instance.addUser(new User(userProp));
        }
    }

    public static UserManager getInstance(){
        return instance;
    }
}
