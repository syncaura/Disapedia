package com.syncaura.disapedia.events;

import com.syncaura.disapedia.DisapediaBot;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class CommandExecutionEvent extends Event {

    private final DisapediaBot bot;
    private final IMessage message;
    private final String command;
    private final String commandKey;
    private final IUser by;
    private String subCommand;
    private String[] args = null;

    public CommandExecutionEvent(DisapediaBot bot, IMessage message, String command, IUser by, String[] args) {
        this.bot = bot;
        this.message = message;
        this.commandKey = command.substring(0, 1);
        this.command = command.substring(1);
        this.by = by;

        if (args == null || args.length < 0) {
            return;
        }

        this.subCommand = args[0]; // We know this is true because of the above check
        this.args = Arrays.copyOfRange(args, 1, args.length);
    }

    public DisapediaBot getBot() {
        return bot;
    }


    public IMessage getMessage() {
        return message;
    }

    public boolean isCommand(String command) {
        return getCommandWithKey().equalsIgnoreCase(command);
    }

    public String getCommand() {
        return command;
    }

    public String getCommandKey() {
        return commandKey;
    }

    public String getCommandWithKey() {
        return getCommandKey() + getCommand();
    }

    public IUser getBy() {
        return by;
    }

    public String getSubCommand() {
        return subCommand;
    }

    public boolean hasSubCommand() {
        return getSubCommand() != null && !getSubCommand().equalsIgnoreCase("");
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgsAsString() {
        return getArgsAsString(" ");
    }

    public String getArgsAsString(String separator) {
        return String.join(separator, getArgs());
    }

    public boolean hasArgs() {
        return getArgs() != null && getArgs().length > 0;
    }
}
