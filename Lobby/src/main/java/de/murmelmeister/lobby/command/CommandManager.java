package de.murmelmeister.lobby.command;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.api.Locations;
import de.murmelmeister.lobby.api.SchedulerTask;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.configs.Message;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.ListUtil;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.lobby.util.config.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandManager extends Commands implements TabExecutor {
    public final ListUtil listUtil;
    public final Config config;
    public final Message message;
    public final SchedulerTask schedulerTask;
    public final Locations locations;

    public CommandManager(Main main) {
        super(main);
        this.listUtil = main.getListUtil();
        this.config = main.getConfig();
        this.message = main.getMessage();
        this.schedulerTask = main.getSchedulerTask();
        this.locations = main.getLocations();
    }

    public void sendMessage(CommandSender sender, String message) {
        if (config.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(HexColor.format(this.message.prefix() + message));
        else sender.sendMessage(HexColor.format(message));
    }

    public boolean isEnable(CommandSender sender, Configs configs) {
        if (!(config.getBoolean(configs))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return false;
        } return true;
    }

    public boolean hasPermission(CommandSender sender, Configs configs) {
        if (!(sender.hasPermission(config.getString(configs)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return false;
        } return true;
    }

    public Player getPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    public boolean existPlayer(CommandSender sender) {
        Player player = getPlayer(sender);
        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return false;
        } else return true;
    }

    public List<String> tabCompletePlayers(CommandSender sender, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        String lastWord = args[args.length - 1];
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        for (Player player : sender.getServer().getOnlinePlayers()) {
            String name = player.getName();
            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord))
                tabComplete.add(name);
        }
        tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
        return tabComplete;
    }

    public List<String> tabCompletePlayers(CommandSender sender, String[] args, int length) {
        if (args.length == length)
            return tabCompletePlayers(sender, args);
        return Collections.emptyList();
    }

    public List<String> tabCompleteOfflinePlayers(CommandSender sender, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        String lastWord = args[args.length - 1];
        for (OfflinePlayer player : sender.getServer().getOfflinePlayers()) {
            String name = player.getName();
            assert name != null;
            if (StringUtil.startsWithIgnoreCase(name, lastWord))
                tabComplete.add(name);
        }
        tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
        return tabComplete;
    }

    public List<String> tabCompleteOfflinePlayers(CommandSender sender, String[] args, int length) {
        if (args.length == length)
            return tabCompleteOfflinePlayers(sender, args);
        return Collections.emptyList();
    }

    public List<String> tabComplete(List<String> list, String[] args) {
        return list.stream().filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
    }

    public List<String> tabComplete(List<String> list, String[] args, int length) {
        if (args.length == length)
            return tabComplete(list, args);
        return Collections.emptyList();
    }
}
