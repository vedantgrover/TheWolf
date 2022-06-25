package com.freyr.thewolf;

import com.freyr.thewolf.listeners.GuildListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

/**
 * Main class where we initialize the bot
 *
 * @author Freyr
 */
public class TheWolf {

    private final @NotNull Dotenv config; // Getting all of my sensitive info from environment file
    private final @NotNull ShardManager shardManager; // Allows bot to run on multiple servers. Bot "builder"

    public TheWolf() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load(); // Initializing and loading the .env file if it exists in the classpath.

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN", System.getenv("TOKEN"))); // Creating a basic instance of the bot and logging in with token
        builder.setStatus(OnlineStatus.ONLINE); // Setting the bot status to ONLINE (Green Dot)
        builder.setActivity(Activity.watching("Freyr fail...")); // Setting the bot activity to "Freyr fail...." (I will change this)
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES); // Enabling Gateway Intents for the bot to have more access to user information (ROLES, MESSAGES)

        // Caches are important to retrieve user data. This would be a requirement in some events
        builder.setMemberCachePolicy(MemberCachePolicy.NONE); // This sets the number of users to Cache in a guild. Lazy loading. Cache's them using Discord.
        builder.setChunkingFilter(ChunkingFilter.NONE); // Forces bot to cache all users on start up (If set to ChunkingFilter.ALL [Make sure to make the memberCachePolicy "all" for the ChunkingFiler.all]).
        // builder.enableCache(); // ONLY TURN THIS ON IF CACHE IS ON. Picks what to cache about the user (ACTIVITY, CLIENT_STATUS, EMOTE, MEMBER_OVERRIDES, ONLINE_STATUS, ROLE_TAGS, VOICE_STATES)

        shardManager = builder.build(); // Creating the bot

        // Registering Listeners
        shardManager.addEventListener(new GuildListener());
    }

    public static void main(String[] args) {
        try {
            new TheWolf(); // Starting the bot
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid"); // Exception handling if the bot token is invalid
        }
    }

    /**
     * Gets the shard manager.
     * The shard manager builds the bot and helps set its properties.
     *
     * @return Shard Manager
     */
    public @NotNull ShardManager getShardManager() {
        return shardManager;
    }

    /**
     * Gets the config variables
     * The config variables are located in the .env file.
     * These variables are secret variables and should not be shared
     *
     * @return Config
     */
    public @NotNull Dotenv getConfig() {
        return config;
    }
}
