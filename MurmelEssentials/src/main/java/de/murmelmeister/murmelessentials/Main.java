package de.murmelmeister.murmelessentials;

import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelessentials.api.CustomPermission;
import de.murmelmeister.murmelessentials.api.Ranks;
import de.murmelmeister.murmelessentials.command.Commands;
import de.murmelmeister.murmelessentials.configs.Config;
import de.murmelmeister.murmelessentials.configs.Message;
import de.murmelmeister.murmelessentials.configs.MySQL;
import de.murmelmeister.murmelessentials.listener.Listeners;
import de.murmelmeister.murmelessentials.util.config.Configs;

import java.sql.Connection;

public class Main {
    private final MurmelEssentials instance;

    private final Config config;
    private final Message message;
    private final MySQL mySQL;

    private Permission permission;

    private Ranks ranks;

    private final Listeners listeners;
    private final Commands commands;

    public Main(MurmelEssentials instance) {
        this.instance = instance;
        this.config = new Config(this);
        this.message = new Message(this);
        this.mySQL = new MySQL(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
    }

    public void disable() {
        mySQL.disconnected();
    }

    public void enable() {
        config.register();
        message.register();
        mySQL.register();

        mySQL.connected();
        tables(mySQL.getConnection());
        register();
        listeners.register();
        commands.register();
        CustomPermission.updateRankPermission(this);
        CustomPermission.updatePermission(this);
        ranks.updatePlayerList(instance, instance.getServer(), permission.getGroup());
        ranks.updatePlayerTeams(instance, instance.getServer(), permission.getGroup());
    }

    private void tables(Connection connection) {
        this.permission = new Permission(connection);
        permission.createAllTables();
        permission.defaultGroup(config.getString(Configs.DEFAULT_GROUP));
    }

    private void register() {
        this.ranks = new Ranks(this);
    }

    public MurmelEssentials getInstance() {
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Permission getPermission() {
        return permission;
    }

    public Ranks getRanks() {
        return ranks;
    }
}
