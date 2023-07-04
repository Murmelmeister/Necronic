package de.murmelmeister.citybuild;

import de.murmelmeister.citybuild.api.Homes;
import de.murmelmeister.citybuild.api.Locations;
import de.murmelmeister.citybuild.api.Ranks;
import de.murmelmeister.citybuild.command.Commands;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.listener.Listeners;
import de.murmelmeister.citybuild.util.ListUtil;

public class Main {
    private final CityBuild instance;
    private final ListUtil listUtil;

    private final Config config;
    private final Message message;
    private final Locations locations;
    private final Homes homes;
    private final Ranks ranks;

    private final Listeners listeners;
    private final Commands commands;

    public Main(CityBuild instance) {
        this.instance = instance;
        this.listUtil = new ListUtil();
        this.config = new Config(this);
        this.message = new Message(this);
        this.locations = new Locations(this);
        this.homes = new Homes(this);
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

    public CityBuild getInstance() {
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

    public Locations getLocations() {
        return locations;
    }

    public Homes getHomes() {
        return homes;
    }

    public Ranks getRanks() {
        return ranks;
    }
}
