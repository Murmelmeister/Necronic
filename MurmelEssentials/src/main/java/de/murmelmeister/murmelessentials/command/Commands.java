package de.murmelmeister.murmelessentials.command;

import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import de.murmelmeister.murmelessentials.command.commands.MurmelEssentialsReloadCommand;
import org.bukkit.command.TabExecutor;

import java.util.Objects;

public class Commands {
    private final Main main;
    private final MurmelEssentials instance;

    public Commands(Main main) {
        this.main = main;
        this.instance = main.getInstance();
    }

    public void register() {
        addCommand("murmelessentialsreload", new MurmelEssentialsReloadCommand(main));
    }

    public void addCommand(String name, TabExecutor executor) {
        Objects.requireNonNull(instance.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(instance.getCommand(name)).setTabCompleter(executor);
    }
}
