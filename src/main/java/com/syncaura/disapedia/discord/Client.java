package com.syncaura.disapedia.discord;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class Client  {

    public static IDiscordClient create(String token, boolean login) {
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(token);

        try {
            if(login) {
                return builder.login();
            }
            return builder.build();
        } catch (DiscordException e) {
            // Well that failed pretty badly
            e.printStackTrace();
        }
        return null; // We want to return null if we fail to create one..
    }

}
