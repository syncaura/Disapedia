package com.syncaura.disapedia.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
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
            } catch (Exception e) {
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

    public String getValueByJsonKey(String jsonKey) {
        if (jsonKey.equalsIgnoreCase("token")) {
            return null; // We don't wanna return this now do we
        }
        Gson gson = new Gson();

        String json = gson.toJson(this);

        JsonObject parsedSettings = gson.fromJson(json, JsonObject.class);

        return getValueByJsonKey(jsonKey, parsedSettings);
    }

    public String getValueByJsonKey(String keyToFind, JsonObject json) {
        try {
            if (keyToFind.equalsIgnoreCase("*")) {
                if (json.has("token")) {
                    json.remove("token");
                }
                return json.toString();
            }

            String jsonKey = keyToFind;

            if (jsonKey.contains(".")) {
                jsonKey = jsonKey.split("\\.")[0]; // Always take the left hand side of the string...
            }

            JsonElement element = json.get(jsonKey);

            if (element == null || element.isJsonNull()) {
                return null;
            }

            if (element.isJsonObject() && keyToFind.contains(".")) {
                String[] splitKeys = keyToFind.split("\\.");
                return getValueByJsonKey(String.join(".", (CharSequence[]) Arrays.copyOfRange(splitKeys, 1, splitKeys.length)), (JsonObject) element);
            }

            return element.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // We didn't find it
        }
    }

}
