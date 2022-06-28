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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

public class QueueCommand extends Command {

    public QueueCommand() {
        super();
        this.name = "queue";
        this.description = "Displays the current queue";
        this.category = Category.MUSIC;

        this.args.add(new OptionData(OptionType.INTEGER, "spot", "Skip to a certain position in the queue!", false));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        int skipTo = event.getOption("spot").getAsInt();
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.getHook().sendMessageEmbeds(EmbedUtils.createError("You need to be in a voice channel for this command to work.")).setEphemeral(true).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (skipTo == 0) {

            LinkedList<AudioTrack> queue = musicManager.scheduler.queue;
            AudioTrack audioTrack = musicManager.audioPlayer.getPlayingTrack();

            if (audioTrack == null) {
                event.getHook().sendMessageEmbeds(EmbedUtils.createError("There is nothing playing right now")).queue();
                return;
            }

            StringBuilder nextUpText = new StringBuilder();
            long totalTime = 0;
            for (int i = 1; i < queue.size(); i++) {
                if (i <= 10) {
                    nextUpText.append(i).append(". ").append(queue.get(i).getInfo().title).append("\n");
                }
                totalTime += queue.get(i).getInfo().length;
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
            embed.setDescription(audioTrack.getInfo().uri);
            embed.addField("Length", dateFormatted, true);
            embed.addField("Artist", audioTrack.getInfo().author, true);
            embed.addField("__Next Up:__", nextUpText.toString(), false);
            embed.setColor(EmbedColor.DEFAULT_COLOR);
            embed.setFooter(((queue.size() > 10) ? "(+" + (queue.size() - 10) + " songs) :: ":"") + "Total Time: " + dateFormatted2);
            embed.setThumbnail(thumbnailURL);

            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else {
            musicManager.audioPlayer.startTrack(musicManager.scheduler.queue.get(skipTo), false);

            // The next 4 lines of code just help format the length of the song (returned in milliseconds) into an aesthetically pleasing format.
            Date date = new Date(musicManager.scheduler.queue.get(skipTo).getInfo().length);
            DateFormat formatter = new SimpleDateFormat("mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateFormatted = formatter.format(date);

            // The next 3 lines of code grabs the URL of the thumbnail
            String url = musicManager.scheduler.queue.get(skipTo).getInfo().uri;
            String videoID = url.substring(32);
            String thumbnailURL = "http://img.youtube.com/vi/" + videoID + "/0.jpg";

            // Building the embed that the user will see
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(musicManager.scheduler.queue.get(skipTo).getInfo().title);
            embed.setDescription(musicManager.scheduler.queue.get(skipTo).getInfo().uri);
            embed.addField("Length", dateFormatted, true);
            embed.addField("Artist", musicManager.scheduler.queue.get(skipTo).getInfo().author, true);
            embed.setColor(EmbedColor.DEFAULT_COLOR);
            embed.setThumbnail(thumbnailURL);
        }
    }
}
