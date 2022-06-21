package com.freyr.thewolf.listeners;

import com.freyr.thewolf.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This is an event listener. All events that are generic to the guild happen here.
 *
 * @author Freyr
 */
public class GuildListener extends ListenerAdapter {

    /**
     * This method fires every time a user joins a server
     *
     * @param event - Has all the information on the event
     * @author Freyr
     */
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        MessageChannel channel = event.getGuild().getChannelById(MessageChannel.class, 988658481932419142L); // Getting the channel through the ID
        VoiceChannel counterChannel = event.getGuild().getVoiceChannelById(988943638476226620L); // Getting the member count channel

        assert counterChannel != null; // Making sure that the channel exists
        counterChannel.getManager().setName("Member Count: " + event.getGuild().getMemberCount()).queue(); // Editing the channel name

        EmbedBuilder embed = new EmbedBuilder(); // Allows us to create and set the properties of an embed
        embed.setColor(EmbedColor.DEFAULT_COLOR); // Sets the color of the embed to the default embed located in EmbedColor.
        embed.setTitle(event.getUser().getName() + " has joined JHS CSHS!"); // Setting the title, or who joined the server
        embed.setDescription("Remember to check " + Objects.requireNonNull(event.getGuild().getRulesChannel()).getAsMention() + "  for full access to the server!"); // Setting the description. Asking them to check the rules channel
        embed.setThumbnail(event.getUser().getAvatarUrl()); // Getting the profile picture of the user and setting it as the thumbnail
        embed.setFooter("Member #" + event.getGuild().getMemberCount()); // Telling the server what the member count is.

        assert channel != null; // Making sure that the channel is not null
        channel.sendMessageEmbeds(embed.build()).queue(); // Sending the welcome message
    }

    /**
     * This method fires every time a user leaves a server (can include getting kicked or banned)
     *
     * @param event - Has all the information on the event
     * @author Freyr
     */
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        MessageChannel channel = event.getGuild().getChannelById(MessageChannel.class, 988945658532728862L); // Getting the leave log channel
        VoiceChannel counterChannel = event.getGuild().getVoiceChannelById(988943638476226620L); // Getting the member count voice channel

        assert counterChannel != null; // Making sure that the channel exists
        counterChannel.getManager().setName("Member Count: " + event.getGuild().getMemberCount()).queue(); // Editing the channel name

        EmbedBuilder embed = new EmbedBuilder(); // Allows us to create and set the properties of an embed
        embed.setDescription("**" + event.getUser().getName() + "** has left the server."); // Bolds the name of the user and formats it as "<user> has left the server."
        embed.setColor(EmbedColor.ERROR_COLOR); // Sets the color of the embed to the ERROR_COLOR in EmbedColor

        assert channel != null; // Making sure that the leave log channel exists
        channel.sendMessageEmbeds(embed.build()).queue(); // Sending the embed
    }

    /**
     * This method fires every time a user sends a message in a server
     *
     * @param event - Has all the information on the event
     * @author Freyr
     */
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
