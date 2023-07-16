package de.murmelmeister.lobby.listener;

import de.murmelmeister.lobby.Lobby;
import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.api.Locations;
import de.murmelmeister.lobby.api.SchedulerTask;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class Listeners implements Listener {
    public final Main main;
    public final Lobby instance;
    public final SchedulerTask schedulerTask;
    public final Locations locations;

    public final Config config;
    public final Message message;

    public Listeners(Main main) {
        this.main = main;
        this.instance = main.getInstance();
        this.config = main.getConfig();
        this.message = main.getMessage();
        this.schedulerTask = main.getSchedulerTask();
        this.locations = main.getLocations();
    }

    public void register() {
    }

    private void addListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public void sendMessage(CommandSender sender, String message) {
        if (config.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(HexColor.format(this.message.prefix() + message));
        else sender.sendMessage(HexColor.format(message));
    }
}
