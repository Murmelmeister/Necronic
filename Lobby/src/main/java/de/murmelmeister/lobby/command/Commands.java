package de.murmelmeister.lobby.command;

import de.murmelmeister.lobby.Lobby;
import de.murmelmeister.lobby.Main;
import org.bukkit.command.TabExecutor;

import java.util.Objects;

public class Commands {
    private final Main main;
    public final Lobby instance;

    public Commands(Main main) {
        this.main = main;
        this.instance = main.getInstance();
    }

    public void register() {
    }

    private void addCommand(String name, TabExecutor executor) {
        Objects.requireNonNull(instance.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(instance.getCommand(name)).setTabCompleter(executor);
    }
}
