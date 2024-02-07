package de.murmelmeister.citybuild;

import com.zaxxer.hikari.HikariDataSource;
import de.murmelmeister.citybuild.api.*;
import de.murmelmeister.citybuild.command.Commands;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.listener.Listeners;
import de.murmelmeister.citybuild.util.ListUtil;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.util.MySQL;
import org.slf4j.Logger;

public class Main {
    private final CityBuild instance;
    private final Logger logger;
    private final ListUtil listUtil;

    private final Config config;
    private final Message message;
    private final SchedulerTask schedulerTask;
    private final Cooldown cooldown;
    private final Locations locations;
    private final Homes homes;
    private final Economy economy;
    private final ItemValue itemValue;
    private final Settings settings;
    private final EnderChest enderChest;

    private Permission permission;

    private final Listeners listeners;
    private final Commands commands;

    public Main(CityBuild instance) {
        this.instance = instance;
        this.logger = instance.getSLF4JLogger();
        this.listUtil = new ListUtil();
        this.config = new Config(this);
        this.message = new Message(this);
        this.schedulerTask = new SchedulerTask(this);
        this.cooldown = new Cooldown(this);
        this.locations = new Locations(this);
        this.homes = new Homes(this);
        this.economy = new Economy(this);
        this.itemValue = new ItemValue(this);
        this.settings = new Settings(this);
        this.enderChest = new EnderChest(this);
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
        itemValue.register();

        MySQL.initConnectionPool();
        tables(MySQL.getDataSource());
        listeners.register();
        commands.register();
        instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
    }

    private void tables(HikariDataSource dataSource) {
        this.permission = new Permission(dataSource);
    }

    public CityBuild getInstance() {
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

    public Cooldown getCooldown() {
        return cooldown;
    }

    public Locations getLocations() {
        return locations;
    }

    public Homes getHomes() {
        return homes;
    }

    public Economy getEconomy() {
        return economy;
    }

    public ItemValue getItemValue() {
        return itemValue;
    }

    public Permission getPermission() {
        return permission;
    }

    public Settings getSettings() {
        return settings;
    }

    public EnderChest getEnderChest() {
        return enderChest;
    }
}
