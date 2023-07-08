package de.murmelmeister.citybuild.command;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.api.*;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.ListUtil;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandManager extends Commands implements TabExecutor {
    public final ListUtil listUtil;
    public final Config config;
    public final Message message;
    public final SchedulerTask schedulerTask;
    public final Cooldown cooldown;
    public final Locations locations;
    public final Homes homes;
    public final Ranks ranks;
    public final Economy economy;

    public CommandManager(Main main) {
        super(main);
        this.listUtil = main.getListUtil();
        this.config = main.getConfig();
        this.message = main.getMessage();
        this.schedulerTask = main.getSchedulerTask();
        this.cooldown = main.getCooldown();
        this.locations = main.getLocations();
        this.homes = main.getHomes();
        this.ranks = main.getRanks();
        this.economy = main.getEconomy();
    }

    public void sendMessage(CommandSender sender, String message) {
        if (config.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(HexColor.format(this.message.prefix() + message));
        else sender.sendMessage(HexColor.format(message));
    }

    public List<String> playerTabComplete(CommandSender sender, String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (args.length == 1) {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            for (Player player : sender.getServer().getOnlinePlayers()) {
                String name = player.getName();
                if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord))
                    tabComplete.add(name);
            }
            tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return tabComplete;
    }

    public List<String> offlinePlayerTabComplete(CommandSender sender, String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (args.length == 1) {
            String lastWord = args[args.length - 1];
            for (OfflinePlayer player : sender.getServer().getOfflinePlayers()) {
                String name = player.getName();
                assert name != null;
                if (StringUtil.startsWithIgnoreCase(name, lastWord))
                    tabComplete.add(name);
            }
            tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return tabComplete;
    }
}
