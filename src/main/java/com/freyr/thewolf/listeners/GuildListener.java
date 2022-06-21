package com.freyr.thewolf.listeners;

import com.freyr.thewolf.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        //Hi hello, 多多 was here and is going to take your beans
        //我喜欢猫咪
        //¿tú supiste que no hablo español?
        //Vedant 给我这个地方所以我现在在写东西
        //Pienso que Vedant esté sospechoso, tal vez él es el impostor

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();

        final String PREFIX = "w.";
        if (msg.getContentRaw().equals(PREFIX + "ping")) {
            long time = System.currentTimeMillis();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(EmbedColor.DEFAULT_COLOR);
            embed.setDescription(":signal_strength: - Calculating...");
            event.getChannel().sendMessageEmbeds(embed.build()).queue(m -> {
                long latency = System.currentTimeMillis() - time;
                EmbedBuilder latencyEmbed = new EmbedBuilder();
                latencyEmbed.setTitle(":ping_pong: - Pong!");
                latencyEmbed.setColor(EmbedColor.DEFAULT_COLOR);
                latencyEmbed.addField("Bot Latency", latency + "ms", false);
                latencyEmbed.addField("Websocket", event.getJDA().getGatewayPing() + "ms", false);
                latencyEmbed.setFooter(event.getAuthor().getName());

                m.editMessageEmbeds(latencyEmbed.build()).queue();
            });
        }
    }
}
