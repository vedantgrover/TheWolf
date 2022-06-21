package com.freyr.thewolf;

import com.freyr.thewolf.listeners.GuildListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class TheWolf {

    private final Dotenv config;
    private final ShardManager shardManager;

    public TheWolf() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load();

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN", System.getenv("TOKEN")));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Freyr fail..."));
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);

        shardManager = builder.build();
        shardManager.addEventListener(
                new GuildListener()
        );
    }

    public static void main(String[] args) {
	    try {
            new TheWolf();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid");
        }
    }
}
