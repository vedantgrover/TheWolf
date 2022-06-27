package com.freyr.thewolf.commands.utility;

import com.freyr.thewolf.commands.Category;
import com.freyr.thewolf.commands.Command;
import com.freyr.thewolf.commands.CommandManager;
import com.freyr.thewolf.listeners.ButtonListener;
import com.freyr.thewolf.util.embeds.EmbedColor;
import com.freyr.thewolf.util.embeds.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.*;

/**
 * This is a dynamic help command. As more commands are added and mapped in {@link CommandManager}, it will automatically
 * add them to the help page.
 *
 * @author Freyr
 */
public class HelpCommand extends Command {

    private static final int COMMANDS_PER_PAGE = 6; // Sets the max number of commands per page

    public HelpCommand() {
        super();
        this.name = "help";
        this.description = "Display a list of all commands and categories.";
        this.category = Category.UTILITY;
        OptionData data = new OptionData(OptionType.STRING, "category", "See commands under this category");
        // Getting all the categories and adding them as choices for the user to choose from
        for (Category c : Category.values()) {
            String name = c.name.toLowerCase();
            data.addChoice(name, name);
        }
        this.args.add(data);
        this.args.add(new OptionData(OptionType.STRING, "command", "See details for this command"));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Create a hashmap that groups commands by categories.
        HashMap<Category, List<Command>> categories = new LinkedHashMap<>();
        EmbedBuilder builder = new EmbedBuilder().setColor(EmbedColor.DEFAULT_COLOR);
        for (Category category : Category.values()) {
            categories.put(category, new ArrayList<>());
        }
        for (Command cmd : CommandManager.commands) {
            categories.get(cmd.category).add(cmd);
        }

        OptionMapping option = event.getOption("category");
        OptionMapping option2 = event.getOption("command");
        if (option != null && option2 != null) {
            event.replyEmbeds(EmbedUtils.createError("Please only give one optional argument and try again.")).queue();
        } else if (option != null) {
            // Display category commands menu
            Category category = Category.valueOf(option.getAsString().toUpperCase());
            List<MessageEmbed> embeds = buildCategoryMenu(category, categories.get(category));
            if (embeds.isEmpty()) {
                // No commands for this category
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(category.emoji + "  **%s Commands**".formatted(category.name))
                        .setDescription("Coming soon...")
                        .setColor(EmbedColor.DEFAULT_COLOR);
                event.replyEmbeds(embed.build()).queue();
                return;
            }
            // Send paginated help menu
            ReplyCallbackAction action = event.replyEmbeds(embeds.get(0));
            if (embeds.size() > 1) {
                ButtonListener.sendPaginatedMenu(event.getUser().getId(), action, embeds);
                return;
            }
            action.queue();
        } else if (option2 != null) {
            // Display command details menu
            Command cmd = CommandManager.mapCommands.get(option2.getAsString());
            if (cmd != null) {
                builder.setTitle("Command: " + cmd.name);
                builder.setDescription(cmd.description);
                StringBuilder usages = new StringBuilder();
                usages.append("`").append(getUsage(cmd)).append("`");
                builder.addField("Usage:", usages.toString(), false);
                event.replyEmbeds(builder.build()).queue();
            } else {
                // Command specified doesn't exist.
                event.replyEmbeds(EmbedUtils.createError("No command called \"" + option2.getAsString() + "\" found.")).queue();
            }
        } else {
            // Display default menu
            builder.setTitle("The Wolf Commands");
            categories.forEach((category, commands) -> {
                String categoryName = category.name().toLowerCase();
                String value = "`/help " + categoryName + "`";
                builder.addField(category.emoji + " " + category.name, value, true);
            });
            event.replyEmbeds(builder.build()).queue();
        }
    }

    /**
     * Builds a menu with all the commands in a specified category.
     *
     * @param category the category to build a menu for.
     * @param commands a list of the commands in this category.
     * @return a list of MessageEmbed objects for pagination.
     */
    public List<MessageEmbed> buildCategoryMenu(Category category, List<Command> commands) {
        List<MessageEmbed> embeds = new ArrayList<>();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(category.emoji + "  **%s Commands**".formatted(category.name));
        embed.setColor(EmbedColor.DEFAULT_COLOR);

        int counter = 0;
        for (Command cmd : commands) {
            embed.appendDescription("`" + getUsage(cmd) + "`\n" + cmd.description + "\n\n");
            counter++;
            if (counter % COMMANDS_PER_PAGE == 0) {
                embeds.add(embed.build());
                embed.setDescription("");
                counter = 0;
            }
        }
        if (counter != 0) embeds.add(embed.build());
        return embeds;
    }

    /**
     * Creates a string of command usage.
     *
     * @param cmd Command to build usage for.
     * @return String with name and args stitched together.
     */
    public String getUsage(Command cmd) {
        StringBuilder usage = new StringBuilder("/" + cmd.name);
        if (cmd.args.isEmpty()) return usage.toString();
        for (int i = 0; i < cmd.args.size(); i++) {
            boolean isRequired = cmd.args.get(i).isRequired();
            if (isRequired) {
                usage.append(" <");
            } else {
                usage.append(" [");
            }
            usage.append(cmd.args.get(i).getName());
            if (isRequired) {
                usage.append(">");
            } else {
                usage.append("]");
            }
        }
        return usage.toString();
    }
}