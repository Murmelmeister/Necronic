package de.murmelmeister.lobby;

import de.murmelmeister.lobby.command.Commands;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.listener.Listeners;

public class Main {
    private final Lobby instance;

    private final Config config;
    private final Message message;

    private final Listeners listeners;
    private final Commands commands;

    public Main(Lobby instance) {
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

    public Lobby getInstance() {
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }
}
