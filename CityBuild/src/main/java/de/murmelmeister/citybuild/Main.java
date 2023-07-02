package de.murmelmeister.citybuild;

import de.murmelmeister.citybuild.command.Commands;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.listener.Listeners;

public class Main {
    private final CityBuild instance;

    private final Config config;
    private final Message message;

    private final Listeners listeners;
    private final Commands commands;

    public Main(CityBuild instance) {
        this.instance = instance;
        this.config = new Config(this);
        this.message = new Message(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
    }

    public void disable() {

    }

    public void enable() {
        config.register();
        message.register();

        listeners.register();
        commands.register();
    }

    public CityBuild getInstance() {
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }
}
