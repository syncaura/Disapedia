package com.syncaura.disapedia.events;

import com.syncaura.disapedia.DisapediaBot;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class CommandExecutionEvent extends Event {

    private final DisapediaBot bot;
    private final IMessage message;
    private final String command;
    private final IUser by;
    private final String[] args;

    public CommandExecutionEvent(DisapediaBot bot, IMessage message, String command, IUser by, String[] args) {
        this.bot = bot;
        this.message = message;
        this.command = command;
        this.by = by;
        this.args = args;
    }

    public DisapediaBot getBot() {
        return bot;
    }

    public String[] getArgs() {
        return args;
    }

    public IMessage getMessage() {
        return message;
    }

    public boolean isCommand(String command) {
        return getCommand().equalsIgnoreCase(command);
    }

    public String getCommand() {
        return command;
    }

    public IUser getBy() {
        return by;
    }
}
