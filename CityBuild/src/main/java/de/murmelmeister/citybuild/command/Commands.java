package de.murmelmeister.citybuild.command;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.commands.EnderChestCommand;
import de.murmelmeister.citybuild.command.commands.CityBuildReloadCommand;
import org.bukkit.command.TabExecutor;

import java.util.Objects;

public class Commands {
    private final Main main;
    public final CityBuild instance;

    public Commands(Main main) {
        this.main = main;
        this.instance = main.getInstance();
    }

    public void register() {
        addCommand("enderchest", new EnderChestCommand(main));
        addCommand("citybuildreload", new CityBuildReloadCommand(main));
    }

    private void addCommand(String name, TabExecutor executor) {
        Objects.requireNonNull(instance.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(instance.getCommand(name)).setTabCompleter(executor);
    }
}
