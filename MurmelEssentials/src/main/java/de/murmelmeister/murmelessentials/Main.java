package de.murmelmeister.murmelessentials;

import de.murmelmeister.murmelessentials.listener.Listeners;

public class Main {
    private final MurmelEssentials instance;

    private final Listeners listeners;

    public Main(MurmelEssentials instance) {
        this.instance = instance;
        this.listeners = new Listeners(this);
    }

    public void disable() {

    }

    public void enable() {
        listeners.register();
    }

    public MurmelEssentials getInstance() {
        return instance;
    }
}
