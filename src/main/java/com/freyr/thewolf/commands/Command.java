package com.freyr.thewolf.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class Command {

    public String name;
    public String description;

    public abstract void execute(SlashCommandInteractionEvent event);
}
