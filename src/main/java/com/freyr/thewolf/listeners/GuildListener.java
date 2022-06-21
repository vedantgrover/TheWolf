package com.freyr.thewolf.listeners;

import com.freyr.thewolf.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * This is an event listener. All events that are generic to the guild happen here.
 *
 * @author Freyr
 */
public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        MessageChannel channel = event.getGuild().getDefaultChannel(); // Getting the default welcome channel in a server

        EmbedBuilder embed = new EmbedBuilder(); // Allows us to create and set the properties of an embed
        embed.setColor(EmbedColor.DEFAULT_COLOR); // Sets the color of the embed to the default embed located in EmbedColor.
        embed.setDescription(event.getMember() + " has joined " + event.getGuild().getName() + "! Remember to check " + event.getGuild().getRulesChannel() + " for full access to the server!"); // Setting the description, or the welcome message of the server

        assert channel != null; // Making sure that the channel is not null
        channel.sendMessageEmbeds(embed.build()).queue(); // Sending the welcome message
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage(); // Getting the message from the user

        final String PREFIX = "w."; // The current prefix of the bot. Will change it to slash commands later
        if (msg.getContentRaw().equals(PREFIX + "ping")) { // Checking to see if the command is w.ping
            long time = System.currentTimeMillis(); // Getting the current time
            EmbedBuilder embed = new EmbedBuilder(); // Allows us to create and set the properties of an embed
            embed.setColor(EmbedColor.DEFAULT_COLOR); // Sets the color of the embed to the default embed color located in EmbedColor
            embed.setDescription(":signal_strength: - Calculating..."); // Sending a message to calculate ping
            event.getChannel().sendMessageEmbeds(embed.build()).queue(m -> { // Editing the message
                long latency = System.currentTimeMillis() - time; // Checking the difference in time between start and message send
                EmbedBuilder latencyEmbed = new EmbedBuilder(); // Allows us to create and set the properties of an embed
                latencyEmbed.setTitle(":ping_pong: - Pong!"); // Setting the title to "🏓 - Pong!"
                latencyEmbed.setColor(EmbedColor.DEFAULT_COLOR); // Sets the color of the embed to the default embed color located in EmbedColor
                latencyEmbed.addField("Bot Latency", latency + "ms", false); // Adds the field called "Bot latency" and sets the field description to the latency
                latencyEmbed.addField("Websocket", event.getJDA().getGatewayPing() + "ms", false); // Adds the field called "Websocket" and sets the description to the websocket latency
                latencyEmbed.setFooter("Requested by " + event.getAuthor().getName()); // Sets the footer to the author

                m.editMessageEmbeds(latencyEmbed.build()).queue(); // Edits the message
            });
        }
    }
}
