package de.murmelmeister.citybuild.command;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.commands.*;
import de.murmelmeister.citybuild.command.commands.homes.AddHomeCommand;
import de.murmelmeister.citybuild.command.commands.homes.HomeCommand;
import de.murmelmeister.citybuild.command.commands.homes.RemoveHomeCommand;
import de.murmelmeister.citybuild.command.commands.locations.*;
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
        addCommand("citybuildreload", new CityBuildReloadCommand(main));
        addCommand("enderchest", new EnderChestCommand(main));
        addCommand("setspawn", new SetSpawnCommand(main));
        addCommand("spawn", new SpawnCommand(main));
        addCommand("removewarp", new RemoveWarpCommand(main));
        addCommand("setwarp", new SetWarpCommand(main));
        addCommand("warp", new WarpCommand(main));
        addCommand("addhome", new AddHomeCommand(main));
        addCommand("home", new HomeCommand(main));
        addCommand("removehome", new RemoveHomeCommand(main));
        addCommand("workbench", new WorkbenchCommand(main));
        addCommand("anvil", new AnvilCommand(main));
        addCommand("trash", new TrashCommand(main));
        addCommand("feed", new FeedCommand(main));
        addCommand("heal", new HealCommand(main));
        addCommand("fly", new FlyCommand(main));
        addCommand("godmode", new GodModeCommand(main));
    }

    private void addCommand(String name, TabExecutor executor) {
        Objects.requireNonNull(instance.getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(instance.getCommand(name)).setTabCompleter(executor);
    }
}
