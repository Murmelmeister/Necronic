package de.murmelmeister.citybuild.listener;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.api.Homes;
import de.murmelmeister.citybuild.api.Locations;
import de.murmelmeister.citybuild.api.Ranks;
import de.murmelmeister.citybuild.api.SchedulerTask;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.listener.listeners.ColorListener;
import de.murmelmeister.citybuild.listener.listeners.ConnectListener;
import de.murmelmeister.citybuild.listener.listeners.DamageListener;
import de.murmelmeister.citybuild.listener.listeners.RankListener;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.ListUtil;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.scoreboard.TestScoreboard;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class Listeners implements Listener {
    public final Main main;
    public final CityBuild instance;
    public final ListUtil listUtil;

    public final Config config;
    public final Message message;
    public final SchedulerTask schedulerTask;
    public final Locations locations;
    public final Homes homes;
    public final Ranks ranks;

    public Listeners(Main main) {
        this.main = main;
        this.instance = main.getInstance();
        this.listUtil = main.getListUtil();
        this.config = main.getConfig();
        this.message = main.getMessage();
        this.schedulerTask = main.getSchedulerTask();
        this.locations = main.getLocations();
        this.homes = main.getHomes();
        this.ranks = main.getRanks();
    }

    public void register() {
        addListener(new ColorListener(main));
        addListener(new DamageListener(main));
        addListener(new RankListener(main));
        addListener(new ConnectListener(main));
    }

    public void updateScoreboard() {
        instance.getServer().getOnlinePlayers().forEach(player -> new TestScoreboard(player, main));
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
