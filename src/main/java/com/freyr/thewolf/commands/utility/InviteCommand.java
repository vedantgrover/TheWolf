package com.freyr.thewolf.commands.utility;

import com.freyr.thewolf.commands.Category;
import com.freyr.thewolf.commands.Command;
import com.freyr.thewolf.util.embeds.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class InviteCommand extends Command {

    public InviteCommand() {
        super();
        this.name = "invite";
        this.description = "Generate an invite to the server or to the bot";
        this.category = Category.UTILITY;

        OptionData data = new OptionData(OptionType.STRING, "type", "Pick between server invite and bot invite", true);
        data.addChoice("server", "server");
        data.addChoice("bot", "bot");

        this.args.add(data);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        String option = event.getOption("type").getAsString();

        if (option.equals("server")) {
            EmbedBuilder serverEmbed = new EmbedBuilder();
            serverEmbed.setTitle("Server Invite");
            serverEmbed.setThumbnail(event.getJDA().getGuildById(988655520082714654L).getIconUrl());
            serverEmbed.setColor(EmbedColor.DEFAULT_COLOR);
            serverEmbed.setDescription(event.getGuild().getDescription());
            serverEmbed.setFooter("Requested by " + event.getUser().getName());
            serverEmbed.addField("Invite", "https://discord.gg/VaZVRpM3mw", false);
            event.getHook().sendMessageEmbeds(serverEmbed.build()).queue();
        } else {
            EmbedBuilder botEmbed = new EmbedBuilder();
            botEmbed.setTitle("Bot Invite");
            botEmbed.setThumbnail("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/53653317-d541-48a0-8252-bc533c980e00/ddk20gg-5d876f13-a2e3-4a64-a589-45e1cee6ee40.png/v1/fill/w_600,h_835,strp/apollo___greek_mythology_by_yliade_ddk20gg-fullview.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzUzNjUzMzE3LWQ1NDEtNDhhMC04MjUyLWJjNTMzYzk4MGUwMFwvZGRrMjBnZy01ZDg3NmYxMy1hMmUzLTRhNjQtYTU4OS00NWUxY2VlNmVlNDAucG5nIiwiaGVpZ2h0IjoiPD04MzUiLCJ3aWR0aCI6Ijw9NjAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLndhdGVybWFyayJdLCJ3bWsiOnsicGF0aCI6Ilwvd21cLzUzNjUzMzE3LWQ1NDEtNDhhMC04MjUyLWJjNTMzYzk4MGUwMFwveWxpYWRlLTQucG5nIiwib3BhY2l0eSI6OTUsInByb3BvcnRpb25zIjowLjQ1LCJncmF2aXR5IjoiY2VudGVyIn19.kJ-P4pwq5NVxQWBqvl_x7s4OQqKyp5GB7202NMCuxP4");
            botEmbed.setColor(EmbedColor.DEFAULT_COLOR);
            botEmbed.setDescription("Thank you for taking an interest in sharing this bot! Sadly this bot has been created only for this server and cannot be shared. However if you would like to invite a similar bot to your own server, try out Apollo18! Apollo is a multi-purpose bot and is currently in development! Important links are below.");
            botEmbed.addField("Server Invite", "https://discord.gg/zqK2PXAuyk", false);
            botEmbed.addField("Bot Invite", "Invite Apollo18 [here](https://discord.com/api/oauth2/authorize?client_id=988814602659856414&permissions=8&scope=bot%20applications.commands)!", true);
            botEmbed.setFooter("Requested by " + event.getUser().getName());
            event.getHook().sendMessageEmbeds(botEmbed.build()).queue();
        }
    }
}
