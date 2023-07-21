package de.murmelmeister.lobby.listener;

import de.murmelmeister.lobby.Lobby;
import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.api.Locations;
import de.murmelmeister.lobby.api.Ranks;
import de.murmelmeister.lobby.api.SchedulerTask;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.listener.listeners.ColorListener;
import de.murmelmeister.lobby.listener.listeners.ConnectListener;
import de.murmelmeister.lobby.listener.listeners.OtherListener;
import de.murmelmeister.lobby.listener.listeners.RankListener;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.ListUtil;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class Listeners implements Listener {
    public final Main main;
    public final Lobby instance;
    public final ListUtil listUtil;
    public final SchedulerTask schedulerTask;
    public final Locations locations;
    public final Ranks ranks;

    public final Config config;
    public final Message message;

    public Listeners(Main main) {
        this.main = main;
        this.instance = main.getInstance();
        this.listUtil = main.getListUtil();
        this.config = main.getConfig();
        this.message = main.getMessage();
        this.schedulerTask = main.getSchedulerTask();
        this.locations = main.getLocations();
        this.ranks = main.getRanks();
    }

    public void register() {
        addListener(new ColorListener(main));
        addListener(new RankListener(main));
        addListener(new OtherListener(main));
        addListener(new ConnectListener(main));
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
