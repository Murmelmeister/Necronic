package de.murmelmeister.lobby;

import de.murmelmeister.lobby.api.Locations;
import de.murmelmeister.lobby.api.SchedulerTask;
import de.murmelmeister.lobby.command.Commands;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.configs.MySQL;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.ListUtil;
import de.murmelmeister.murmelapi.permission.Permission;

import java.sql.Connection;

public class Main {
    private final Lobby instance;
    private final ListUtil listUtil;

    private final Config config;
    private final Message message;
    private final MySQL mySQL;
    private final SchedulerTask schedulerTask;
    private final Locations locations;

    private Permission permission;

    private final Listeners listeners;
    private final Commands commands;

    public Main(Lobby instance) {
        this.instance = instance;
        this.listUtil = new ListUtil();
        this.config = new Config(this);
        this.message = new Message(this);
        this.mySQL = new MySQL(this);
        this.schedulerTask = new SchedulerTask(this);
        this.locations = new Locations(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
    }

    public void disable() {
        instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        mySQL.disconnected();
    }

    public void enable() {
        config.register();
        message.register();
        mySQL.register();
        locations.create();

        mySQL.connected();
        tables(mySQL.getConnection());
        listeners.register();
        commands.register();
        instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
    }

    private void tables(Connection connection) {
        this.permission = new Permission(connection);
    }

    public Lobby getInstance() {
        return instance;
    }

    public ListUtil getListUtil() {
        return listUtil;
    }

    public Config getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }

    public SchedulerTask getSchedulerTask() {
        return schedulerTask;
    }

    public Locations getLocations() {
        return locations;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Permission getPermission() {
        return permission;
    }
}
