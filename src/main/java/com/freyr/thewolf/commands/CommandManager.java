package com.freyr.thewolf.commands;

import com.freyr.thewolf.commands.utility.PingCommand;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will handle all the commands that I make and add them into Discord.
 * <p>
 * Guild Commands - These commands can be added per guild and are only available within that guild. (Max: 100)
 * Global Commands - These commands are available across all servers. It takes up to an hour sometimes more to register. (Max: unlimited)
 *
 * @author Freyr
 */
public class CommandManager extends ListenerAdapter {

    public static final List<Command> commands = new ArrayList<>();

    public static final Map<String, Command> mapCommands = new HashMap<>();

    public CommandManager() {
        mapCommands(new PingCommand());
    }

    private void mapCommands(Command... cmds) {
        for (Command cmd : cmds) {
            mapCommands.put(cmd.name, cmd);
            commands.add(cmd);
        }
    }

    public List<CommandData> unpackCommandData() {
        List<CommandData> commandData = new ArrayList<>();
        for (Command cmd : commands) {
            commandData.add(Commands.slash(cmd.name, cmd.description));
        }

        return commandData;
    }

    /**
     * This method fires everytime someone uses a slash command.
     *
     * @param event Has all the information about the event.
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Command cmd = mapCommands.get(event.getName());
        if (cmd != null) {
            cmd.execute(event);
        }
    }

    /**
     * This method fires everytime the guild has been loaded up for the bot.
     * I will be using this method mostly for testing purposes (Creating guild commands and testing)
     *
     * @param event Has all the information about the event.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        if (event.getGuild().getIdLong() == 988655520082714654L) {
            event.getGuild().updateCommands().addCommands(unpackCommandData()).queue();
        }
    }

    /**
     * This method fires everytime the bot is ready. (Everytime it starts up)
     * This method will hold the global commands, and I will be using it to register commands for the server to use.
     *
     * @param event Has all the information about the event.
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>(); // Holds all command data
        // Adding two slash commands into the command data
        commandData.add(Commands.slash("invite", "Create an invite for the server or for the bot!"));
        commandData.add(Commands.slash("ping", "Returns the latency of the bot and the Discord API"));
        event.getJDA().updateCommands().addCommands(commandData).queue(); // Adding all commands into Discord.

        // I will be making this more advanced and easy to use. Don't worry. I will fix this.
    }
}
