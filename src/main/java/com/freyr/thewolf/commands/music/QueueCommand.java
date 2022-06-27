package com.freyr.thewolf.commands.music;

import com.freyr.thewolf.commands.Category;
import com.freyr.thewolf.commands.Command;
import com.freyr.thewolf.util.embeds.EmbedColor;
import com.freyr.thewolf.util.embeds.EmbedUtils;
import com.freyr.thewolf.util.music.GuildMusicManager;
import com.freyr.thewolf.util.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

public class QueueCommand extends Command {

    public QueueCommand() {
        super();
        this.name = "queue";
        this.description = "Displays the current queue";
        this.category = Category.MUSIC;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        final Member self = event.getGuild().getSelfMember();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.getHook().sendMessageEmbeds(EmbedUtils.createError("You need to be in a voice channel for this command to work.")).setEphemeral(true).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        AudioTrack audioTrack = musicManager.audioPlayer.getPlayingTrack();

        if (audioTrack == null) {
            event.getHook().sendMessageEmbeds(EmbedUtils.createError("There is nothing playing right now")).queue();
            return;
        }

        int counter = 1;
        String nextUpText = "";
        long totalTime = 0;

        for (AudioTrack track : queue) {
            if (counter <= 10) {
                nextUpText += counter + ". " + track.getInfo().title + "\n";
            }
            totalTime += track.getInfo().length;
            counter++;
        }

        Date date = new Date(audioTrack.getInfo().length);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);

        Date date2 = new Date(totalTime);
        DateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted2 = formatter2.format(date2);

        String url = audioTrack.getInfo().uri;
        String videoID = url.substring(32);
        String thumbnailURL = "http://img.youtube.com/vi/" + videoID + "/0.jpg";

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(audioTrack.getInfo().title);
        embed.addField("Length", dateFormatted, true);
        embed.addField("Artist", audioTrack.getInfo().author, true);
        embed.addField("__Next Up:__", nextUpText, false);
        embed.setColor(EmbedColor.DEFAULT_COLOR);
        embed.setFooter("Total Time: " + dateFormatted2);
        embed.setThumbnail(thumbnailURL);

        event.getHook().sendMessageEmbeds(embed.build()).queue();

    }
}
