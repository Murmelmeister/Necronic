package de.murmelmeister.murmelessentials.listener;

import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import de.murmelmeister.murmelessentials.api.Ranks;
import de.murmelmeister.murmelessentials.configs.Config;
import de.murmelmeister.murmelessentials.configs.Message;
import de.murmelmeister.murmelessentials.listener.listeners.ColorListener;
import de.murmelmeister.murmelessentials.listener.listeners.PermissionListener;
import de.murmelmeister.murmelessentials.listener.listeners.RankListener;
import de.murmelmeister.murmelessentials.util.HexColor;
import de.murmelmeister.murmelessentials.util.config.Configs;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class Listeners implements Listener {
    public final Main main;
    public final MurmelEssentials instance;

    public final Config config;
    public final Message message;
    public final Permission permission;
    public final Ranks ranks;

    public Listeners(Main main) {
        this.main = main;
        this.instance = main.getInstance();
        this.config = main.getConfig();
        this.message = main.getMessage();
        this.permission = main.getPermission();
        this.ranks = main.getRanks();
    }

    public void register() {
        addListener(new PermissionListener(main));
        addListener(new ColorListener(main));
        addListener(new RankListener(main));
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
