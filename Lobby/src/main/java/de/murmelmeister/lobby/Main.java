package de.murmelmeister.lobby;

import de.murmelmeister.lobby.api.Locations;
import de.murmelmeister.lobby.api.Ranks;
import de.murmelmeister.lobby.api.SchedulerTask;
import de.murmelmeister.lobby.command.Commands;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.ListUtil;

public class Main {
    private final Lobby instance;
    private final ListUtil listUtil;

    private final Config config;
    private final Message message;
    private final SchedulerTask schedulerTask;
    private final Locations locations;
    private final Ranks ranks;

    private final Listeners listeners;
    private final Commands commands;

    public Main(Lobby instance) {
        this.instance = instance;
        this.listUtil = new ListUtil();
        this.config = new Config(this);
        this.message = new Message(this);
        this.schedulerTask = new SchedulerTask(this);
        this.locations = new Locations(this);
        this.ranks = new Ranks(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
    }

    public void disable() {

    }

    public void enable() {
        config.register();
        message.register();
        locations.create();
        ranks.register();

        listeners.register();
        commands.register();
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

    public Ranks getRanks() {
        return ranks;
    }
}
