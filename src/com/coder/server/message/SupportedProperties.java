package com.coder.server.message;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ziver on 2015-11-03.
 */
public class SupportedProperties extends HashMap<String,ArrayList<String>> {

    public void addSupportedPropertyValue(String property, String supportedValue){
        if(!this.containsKey(property))
            this.put(property, new ArrayList<>());
        this.get(property).add(supportedValue);
    }
}
