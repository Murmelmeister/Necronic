package de.murmelmeister.murmelessentials;

import com.zaxxer.hikari.HikariDataSource;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.util.MySQL;
import de.murmelmeister.murmelessentials.api.CustomPermission;
import de.murmelmeister.murmelessentials.api.Ranks;
import de.murmelmeister.murmelessentials.command.Commands;
import de.murmelmeister.murmelessentials.configs.Config;
import de.murmelmeister.murmelessentials.configs.Message;
import de.murmelmeister.murmelessentials.listener.Listeners;
import de.murmelmeister.murmelessentials.util.config.Configs;
import org.slf4j.Logger;

public class Main {
    private final MurmelEssentials instance;
    private final Logger logger;

    private final Config config;
    private final Message message;

    private Permission permission;

    private Ranks ranks;

    private final Listeners listeners;
    private final Commands commands;

    public Main(MurmelEssentials instance) {
        this.instance = instance;
        this.logger = instance.getSLF4JLogger();
        this.config = new Config(this);
        this.message = new Message(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
    }

    public void disable() {
        MySQL.closeConnectionPool();
    }

    public void enable() {
        config.register();
        message.register();
        MySQL.registerFile(logger, String.format("plugins//%s//", config.getString(Configs.FILE_NAME)), "mysql");

        MySQL.initConnectionPool();
        tables(MySQL.getDataSource());
        register();
        listeners.register();
        commands.register();
        CustomPermission.updatePermission(this);
        ranks.updatePlayerList(instance, instance.getServer(), permission);
        ranks.updatePlayerTeams(instance, instance.getServer(), permission);
    }

    private void tables(HikariDataSource dataSource) {
        this.permission = new Permission(dataSource);
    }

    private void register() {
        this.ranks = new Ranks();
    }

    public MurmelEssentials getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public Config getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }

    public Permission getPermission() {
        return permission;
    }

    public Ranks getRanks() {
        return ranks;
    }
}
