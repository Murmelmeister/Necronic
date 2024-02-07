package de.murmelmeister.lobby;

import com.zaxxer.hikari.HikariDataSource;
import de.murmelmeister.lobby.api.Locations;
import de.murmelmeister.lobby.api.SchedulerTask;
import de.murmelmeister.lobby.command.Commands;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.ListUtil;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.util.MySQL;
import org.slf4j.Logger;

public class Main {
    private final Lobby instance;
    private final Logger logger;
    private final ListUtil listUtil;

    private final Config config;
    private final Message message;
    private final SchedulerTask schedulerTask;
    private final Locations locations;

    private Permission permission;

    private final Listeners listeners;
    private final Commands commands;

    public Main(Lobby instance) {
        this.instance = instance;
        this.logger = instance.getSLF4JLogger();
        this.listUtil = new ListUtil();
        this.config = new Config(this);
        this.message = new Message(this);
        this.schedulerTask = new SchedulerTask(this);
        this.locations = new Locations(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
    }

    public void disable() {
        instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        MySQL.closeConnectionPool();
    }

    public void enable() {
        config.register();
        message.register();
        MySQL.registerFile(logger, String.format("plugins//%s//", config.getString(Configs.FILE_NAME)), "mysql");
        locations.create();

        MySQL.initConnectionPool();
        tables(MySQL.getDataSource());
        listeners.register();
        commands.register();
        instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
    }

    private void tables(HikariDataSource dataSource) {
        this.permission = new Permission(dataSource);
    }

    public Lobby getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
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

    public Permission getPermission() {
        return permission;
    }
}
