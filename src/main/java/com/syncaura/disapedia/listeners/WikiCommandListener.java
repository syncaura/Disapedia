package com.syncaura.disapedia.listeners;

import com.syncaura.disapedia.DisapediaBot;
import com.syncaura.disapedia.config.WikiData;
import com.syncaura.disapedia.events.CommandExecutionEvent;
import com.syncaura.disapedia.wiki.MediaWiki;
import com.syncaura.disapedia.wiki.SearchResult;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.impl.obj.PrivateChannel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class WikiCommandListener implements IListener<CommandExecutionEvent> {

    private DisapediaBot bot;

    public WikiCommandListener(DisapediaBot bot) {
        this.bot = bot;
        bot.getClient().getDispatcher().registerListener(this);
    }

    @Override
    public void handle(CommandExecutionEvent event) {
        if (!event.getCommand().equalsIgnoreCase("wiki")) {
            return;
        }

        try {
            MessageBuilder messageBuilder = new MessageBuilder(event.getClient());
            messageBuilder.withChannel(event.getMessage().getChannel());

            if (!event.hasSubCommand()) { // TODO: Make a default handler for sub commands C:
                messageBuilder.withContent("Could not determine sub command, please type !wiki help for a list of sub commands.").send();
                return;
            }

            EnumSet<Permissions> userPermissions = event.getBy().getPermissionsForGuild(event.getMessage().getGuild());

            // TODO: Make these use classes instead of methods... could get messy otherwise
            // TODO: REALLY CAL, THIS NEEDS TO CHANGE SOON.
            switch (event.getSubCommand()) {
                case "search":
                    handleSearchCommand(messageBuilder, event);
                    break;
                case "help":
                    handleHelpCommand(event.getMessage().getChannel(), event.getBy());
                    event.getMessage().delete(); // We always want to delete the help command..
                    break;
                case "current":
                    handleCurrentCommand(event);
                    break;
                case "settings":
                    if (!userPermissions.contains(Permissions.MANAGE_SERVER)) {
                        sendNoPermission(event, event.getBy());
                        return;
                    }
                    handleSettingsCommand(event);
                    break;
                default:
                    messageBuilder.withContent("Could not determine sub command, please type !wiki help for a list of sub commands.").send();
                    break;
            }

            if (bot.getSettings().shouldDeleteCommandInput() && !event.getMessage().isDeleted()) {
                event.getMessage().delete();
            }
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            e.printStackTrace();
        } catch (MissingPermissionsException e) {
            e.printStackTrace();
        }
    }

    private void handleHelpCommand(IChannel channel, IUser by) throws RateLimitException, DiscordException, MissingPermissionsException {
        MessageBuilder message = new MessageBuilder(bot.getClient());

        message.withChannel(bot.getClient().getOrCreatePMChannel(by));

        message.withContent("Disapedia is a discord bot that allows you to search wiki pages for topics/resources of your interest.");
        message.appendContent("\nAvailable commands:", MessageBuilder.Styles.UNDERLINE_BOLD);
        message.appendContent("\n\n!wiki help - Displays help message to you");
        message.appendContent("\n!wiki help <user> - Displays help message to user that you specify. Example: @Disapedia#2685");
        message.appendContent("\n!wiki search <query> - Searches the default (configured) wiki for that topic/resource");

        if (by.getPermissionsForGuild(channel.getGuild()).contains(Permissions.MANAGE_SERVER)) {
            message.appendContent("\n!wiki settings reload - Reloads the configuration file");
            message.appendContent("\n!wiki settings get <jsonKey> - Gets value in settings.json at the specified key.");
        }

        message.appendContent("\n").appendContent("Created by the syncaura team with :heart: | https://syncaura.com", MessageBuilder.Styles.ITALICS);

        message.send();
    }

    private void handleCurrentCommand(CommandExecutionEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
        MessageBuilder messageBuilder = new MessageBuilder(bot.getClient());
        messageBuilder.withChannel(event.getMessage().getChannel()); // We want to send this message in the current channel as it's info
        messageBuilder.withContent("Disapedia is currently searching the following wiki: ");
        messageBuilder.appendContent(bot.getSettings().getWikiData().getWikiUrl(), MessageBuilder.Styles.UNDERLINE_BOLD);
        messageBuilder.send();
    }

    private void handleSearchCommand(MessageBuilder messageBuilder, CommandExecutionEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (!event.hasArgs()) {
            messageBuilder.withContent("Please give me a search query!").send();
            return;
        }

        WikiData wikiData = bot.getSettings().getWikiData();

        MediaWiki wiki = new MediaWiki(wikiData.getWikiUrl());

        SearchResult search = wiki.search(event.getArgsAsString(), bot.getSettings().getWikiData().getLanguage());

        if (!search.isValid()) {
            messageBuilder.withContent("Could not find anything for search query '" + search.getQuery() + "'.").send();
            return;
        }

        if (search.getUrls().length == 1) {
            messageBuilder.withContent("You can find out more about '" + search.getQuery() + "' here: " + search.getUrls()[0]).send();
            return;
        }

        messageBuilder.withContent("Here's what I found for the search query '" + search.getQuery() + "'\n------\n" + String.join("\n--\n", search.getUrls()));
        messageBuilder.send();
    }

    private void handleSettingsCommand(CommandExecutionEvent event) {
        try {
            if (!event.hasArgs()) {
                handleHelpCommand(event.getMessage().getChannel(), event.getBy());
                return;
            }
            String subCommand = event.getArgs()[0];
            switch (subCommand) {
                case "reload":
                    long beforeReloadTime = System.currentTimeMillis();
                    bot.reloadSettings();
                    new MessageBuilder(bot.getClient())
                            .withChannel(event.getMessage().getChannel())
                            .withContent("Reloaded settings file for Disapedia in " + (System.currentTimeMillis() - beforeReloadTime) + "ms!", MessageBuilder.Styles.BOLD)
                            .send();
                    break;
                case "set":
                    break;
                case "get":
                    MessageBuilder messageBuilder = new MessageBuilder(bot.getClient())
                            .withChannel(event.getMessage().getChannel());

                    String[] args = Arrays.copyOfRange(event.getArgs(), 1, event.getArgs().length);
                    if (args.length == 0) {
                        messageBuilder.withContent("Please specify the option to retrieve from settings!")
                                .send();
                        return; // We need than that
                    }
                    messageBuilder.withContent("Value of '")
                            .appendContent(String.join(" ", args), MessageBuilder.Styles.BOLD)
                            .appendContent("' is set to:\n")
                            .appendContent(bot.getSettings().getValueByJsonKey(args[0]))
                            .send();
                    break;
            }
        } catch (Exception e) {
            // todo: add stuff
            e.printStackTrace();
        }
    }

    private void sendNoPermission(CommandExecutionEvent event, IUser by) throws RateLimitException, DiscordException, MissingPermissionsException {
        IPrivateChannel privateChannel = bot.getClient().getOrCreatePMChannel(by);
        MessageBuilder messageBuilder = new MessageBuilder(bot.getClient()).withChannel(privateChannel);

        messageBuilder.withContent("You tried to perform the command '")
                .appendContent(event.getMessage().getContent().substring(1), MessageBuilder.Styles.BOLD)
                .appendContent("' but you do not have permission to do this in the '")
                .appendContent(event.getMessage().getChannel().getGuild().getName(), MessageBuilder.Styles.BOLD)
                .appendContent("' guild.\n\nIf you believe this is an error please contact the guilds server admins!").send();
        event.getMessage().delete();
    }
}
