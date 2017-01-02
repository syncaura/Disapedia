package com.syncaura.disapedia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syncaura.disapedia.config.Settings;
import com.syncaura.disapedia.discord.Client;
import com.syncaura.disapedia.listeners.CommandProcessorListener;
import com.syncaura.disapedia.listeners.WikiCommandListener;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.DiscordException;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class DisapediaBot {

    public static DisapediaBot INSTANCE;

    private IDiscordClient client;

    private Settings settings;

    public DisapediaBot(IDiscordClient client, Settings settings) {
        this.client = client;
        this.settings = settings;
        setup();
    }

    /**
     * Entry point of the application
     **/
    public static void main(String[] args) throws Exception {
        try {
            login(loadSettingsFromDisk());
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    private static Settings loadSettingsFromDisk() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        InputStream settingsFile = Settings.loadSettings();

        if (settingsFile == null) {
            throw new Exception("Settings file could not be found!");
        }

        Settings settings = gson.fromJson(new InputStreamReader(settingsFile), Settings.class);

        if (settings == null || settings.getToken().equalsIgnoreCase("")) {
            throw new Exception("Failed to parse settings file or it not contain an API token!");
        }

        return settings;
    }

    public static void login(Settings settings) throws DiscordException {
        IDiscordClient client = Client.create(settings.getToken(), true); // This will need to load in from file....

        if (client == null) {
            return; // We know that the API has already thrown a stacktrace here... might need to make sure we exit hard! >:(
        }

        INSTANCE = new DisapediaBot(client, settings);
    }

    public IDiscordClient getClient() {
        return client;
    }

    public Settings getSettings() {
        return settings;
    }

    public boolean reloadSettings() {
        try {
            settings = loadSettingsFromDisk();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setup() {
        try {
            // TODO: Add in a display name option to change bot name and make sure the status changes also
            getClient().changeStatus(Status.game(getSettings().getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerEventListeners();
    }

    public void registerEventListeners() {
        new CommandProcessorListener(this);
        new WikiCommandListener(this);
    }
}
