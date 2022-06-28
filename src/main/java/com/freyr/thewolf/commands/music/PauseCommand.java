package com.freyr.thewolf.commands.music;

import com.freyr.thewolf.commands.Category;
import com.freyr.thewolf.commands.Command;
import com.freyr.thewolf.util.embeds.EmbedUtils;
import com.freyr.thewolf.util.music.GuildMusicManager;
import com.freyr.thewolf.util.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PauseCommand extends Command {

    public PauseCommand() {
        super();
        this.name = "pause";
        this.description = "pauses your music";
        this.category = Category.MUSIC;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.getHook().sendMessageEmbeds(EmbedUtils.createError("You need to be in a voice channel for this command to work.")).setEphemeral(true).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        musicManager.audioPlayer.setPaused(true);

        event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Successfully paused your song")).queue();
    }
}
