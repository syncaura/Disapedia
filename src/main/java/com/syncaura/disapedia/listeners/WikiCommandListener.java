package com.syncaura.disapedia.listeners;

import com.syncaura.disapedia.DisapediaBot;
import com.syncaura.disapedia.events.CommandExecutionEvent;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.util.MessageBuilder;

import java.util.Arrays;

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
        try {
            MessageBuilder builder = new MessageBuilder(event.getClient());
            builder.withChannel(event.getMessage().getChannel());
            builder.withContent("Command received: " + event.getCommand() + " " + Arrays.toString(event.getArgs()));
            builder.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
