package de.murmelmeister.lobby.command;

import de.murmelmeister.lobby.Lobby;
import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.command.commands.*;
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
        addCommand("setspawn", new SetSpawnCommand(main));
        addCommand("spawn", new SpawnCommand(main));
        addCommand("build", new BuildCommand(main));
        addCommand("setspawnheight", new SetSpawnHeightCommand(main));
        addCommand("spawnheight", new SpawnHeightCommand(main));
    }

    private void addCommand(String name, TabExecutor executor) {
        Objects.requireNonNull(instance.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(instance.getCommand(name)).setTabCompleter(executor);
    }
}
