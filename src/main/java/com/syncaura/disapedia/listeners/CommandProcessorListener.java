package com.syncaura.disapedia.listeners;

import com.syncaura.disapedia.DisapediaBot;
import com.syncaura.disapedia.events.CommandExecutionEvent;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class CommandProcessorListener implements IListener<MessageReceivedEvent> {

    private DisapediaBot bot;

    public CommandProcessorListener(DisapediaBot bot) {
        this.bot = bot;
        bot.getClient().getDispatcher().registerListener(this);
    }

    public void handle(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        IChannel channel = message.getChannel();

        if (!bot.getSettings().getChannels().contains(channel.getName())) {
            return; // Don't process this channel because it's not configured for use :) (Could also be done with Discord permissions?...)
        }

        String messageContent = message.getContent();

        if (!messageContent.startsWith(bot.getSettings().getCommandKey())) {
            return;
        }

        String command = messageContent.toLowerCase();
        String[] commandArgs = null;

        if (messageContent.contains(" ")) {
            command = command.trim().split(" ")[0];
            commandArgs = messageContent.substring(messageContent.indexOf(' ') + 1).split("\\s+");
        }

        CommandExecutionEvent commandExecutionEvent = new CommandExecutionEvent(bot, message, command, message.getAuthor(), commandArgs);
        bot.getClient().getDispatcher().dispatch(commandExecutionEvent);

        if(!bot.getSettings().shouldDeleteCommandInput()) {
            return;
        }

        try {
            message.delete();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: Handle me
        }
    }
}
