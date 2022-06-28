package com.freyr.thewolf.util.music;

import com.freyr.thewolf.util.embeds.EmbedColor;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    public static MessageChannel musicLogChannel;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandInteractionEvent event, MessageChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getJDA().getGuildById(988655520082714654L));
        this.musicLogChannel = channel;

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                Date date = new Date(audioTrack.getInfo().length);
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateFormatted = formatter.format(date);

                String url = audioTrack.getInfo().uri;
                String videoID = url.substring(32);
                String thumbnailURL = "http://img.youtube.com/vi/" + videoID + "/0.jpg";


                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(audioTrack.getInfo().title);
                embed.setDescription("Song added to queue. Number: " + musicManager.scheduler.queue.indexOf(audioTrack));
                embed.addField("Length", dateFormatted, true);
                embed.addField("Artist", audioTrack.getInfo().author, true);
                embed.setColor(EmbedColor.DEFAULT_COLOR);
                embed.setThumbnail(thumbnailURL);

                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (audioPlaylist.isSearchResult()) {
                    trackLoaded(tracks.get(0));
                } else {
                    for (AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }

                    EmbedBuilder embed = new EmbedBuilder();

                    embed.setTitle(audioPlaylist.getName());
                    embed.setDescription("Added " + tracks.size() + " songs in the queue.");
                    embed.setColor(EmbedColor.DEFAULT_COLOR);

                    event.getHook().sendMessageEmbeds(embed.build()).queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }
}
