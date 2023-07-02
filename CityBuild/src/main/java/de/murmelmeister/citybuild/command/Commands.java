package de.murmelmeister.citybuild.command;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import org.bukkit.command.TabExecutor;

import java.util.Objects;

public class Commands {
    public final CityBuild instance;

    public Commands(Main main) {
        this.instance = main.getInstance();
    }

    public void register() {

    }

    private void addCommand(String name, TabExecutor executor) {
        Objects.requireNonNull(instance.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(instance.getCommand(name)).setTabCompleter(executor);
    }
}
