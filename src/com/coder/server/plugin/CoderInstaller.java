package com.coder.server.plugin;

/**
 * A interface that handles the environment where the ProjectType plugin operates in.
 * This class will setup the required environment or update it.
 */
public interface CoderInstaller {

    /**
     * @return if the required environment is installed
     */
    public boolean isInstalled();

    /**
     * Will install/setup the needed environment
     */
    public void install() throws Exception;


    //public boolean isUpToDate();
    //public void update() throws Exception;
}
