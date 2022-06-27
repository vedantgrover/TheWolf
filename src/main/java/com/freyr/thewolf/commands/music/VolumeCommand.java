package com.freyr.thewolf.commands.music;

import com.freyr.thewolf.commands.Category;
import com.freyr.thewolf.commands.Command;
import com.freyr.thewolf.util.embeds.EmbedUtils;
import com.freyr.thewolf.util.music.GuildMusicManager;
import com.freyr.thewolf.util.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VolumeCommand extends Command {

    public VolumeCommand() {
        super();
        this.name = "volume";
        this.description = "Sets the volume of the music";
        this.category = Category.MUSIC;

        OptionData data = new OptionData(OptionType.INTEGER, "volume", "New Volume Percent", true);
        data.setMaxValue(200);
        data.setMinValue(0);
        this.args.add(new OptionData(OptionType.INTEGER, "volume", "The volume percent", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        final Member self = event.getGuild().getSelfMember();
        int volume = event.getOption("volume").getAsInt();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.getHook().sendMessageEmbeds(EmbedUtils.createError("You need to be in a voice channel for this command to work.")).setEphemeral(true).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        musicManager.audioPlayer.setVolume(volume);

        event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Volume has been set to " + musicManager.audioPlayer.getVolume() + "%")).queue();
    }
}
