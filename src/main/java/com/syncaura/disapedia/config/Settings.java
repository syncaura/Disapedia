package com.syncaura.disapedia.config;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class Settings {

    private String token;
    private String displayName;
    private String status;
    private List<String> channels;

    @SuppressWarnings("FieldCanBeLocal")
    private String commandKey = "!"; // Default this

    private boolean deleteCommandInput;
    private WikiData wiki;

    /**
     * This will load from the disk every time this is called so make sure that we are only calling this when it's needed
     *
     * @return InputStream of the settings file
     */
    public static InputStream loadSettings() {
        File savedSettings = new File("settings.json");

        if (savedSettings.exists()) {
            try {
                return new FileInputStream(savedSettings);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return getDefaultSettings();
    }

    /**
     * Gets the discord bot API token
     *
     * @return API Token
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the discord bot display name
     *
     * @return Discord bot display name
     */
    public String getDisplayName() {
        return displayName;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getChannels() {
        return channels;
    }

    public static boolean saveDefaultSettings() {
        try {
            Files.copy(getDefaultSettings(), Paths.get("settings.json"), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static InputStream getDefaultSettings() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream("settings.json");
    }

    public String getCommandKey() {
        return commandKey;
    }

    public boolean shouldDeleteCommandInput() {
        return deleteCommandInput;
    }

    public WikiData getWikiData() {
        return wiki;
    }
}
