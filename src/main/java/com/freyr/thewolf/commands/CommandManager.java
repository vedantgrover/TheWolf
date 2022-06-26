package com.freyr.thewolf.commands;

import com.freyr.thewolf.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle all the commands that I make and add them into Discord.
 * <p>
 * Guild Commands - These commands can be added per guild and are only available within that guild. (Max: 100)
 * Global Commands - These commands are available across all servers. It takes up to an hour sometimes more to register. (Max: unlimited)
 *
 * @author Freyr
 */
public class CommandManager extends ListenerAdapter {

    /**
     * This method fires everytime someone uses a slash command.
     *
     * @param event Has all the information about the event.
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        /* I will be updating this handler to a much more advanced handler soon. Just for now, I am keeping it like this.
        This hurt to wrote, but I promise I will fix this 😇 */

        String command = event.getName(); // Getting the command name (Ex. /ping - Returns "ping")
        if (command.equals("invite")) {
            String serverImageURL = event.getGuild().getIconUrl(); // Getting the logo of the server
            MessageEmbed embed = new EmbedBuilder().setTitle("Invites").setDescription("Coming soon!").setColor(EmbedColor.DEFAULT_COLOR).setThumbnail(serverImageURL).build(); // Creating a simple coming soon embed
            event.replyEmbeds(embed).queue(); // Replying to the command
        } else if (command.equals("ping")) {
            event.deferReply().queue(); // Asking discord to wait more than 3 seconds for the command to compute and return a response
            long time = System.currentTimeMillis(); // Getting the current time
            MessageEmbed embed = new EmbedBuilder().setColor(EmbedColor.DEFAULT_COLOR).setDescription(":signal_strength: - Calculating...").build(); // Creating an embed to send. (We will use the time it took to create and send this embed as the latency)
            event.getHook().sendMessageEmbeds(embed).queue(m -> {
                long latency = System.currentTimeMillis() - time; // Getting the latency
                MessageEmbed latencyEmbed = new EmbedBuilder().setTitle(":ping_pong: Pong!").setColor(EmbedColor.DEFAULT_COLOR).addField("Bot Latency", latency + "ms", false).addField("Discord API", event.getJDA().getGatewayPing() + "ms", false).setFooter("Requested by " + event.getUser().getName()).build(); // Creating the embed that displays the information
                m.editMessageEmbeds(latencyEmbed).queue(); // Editing the embed previously to be the new embed
            });
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
    }

    /**
     * This method fires everytime the bot is ready. (Everytime it starts up)
     * This method will hold the global commands and I will be using it to register commands for the server to use.
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
