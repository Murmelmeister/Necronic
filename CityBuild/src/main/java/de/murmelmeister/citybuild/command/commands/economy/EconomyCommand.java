package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EconomyCommand extends CommandManager {
    public EconomyCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ECONOMY_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ECONOMY_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        if (args.length >= 2) {
            switch (args[0]) {
                case "set":
                    economySet(sender, args);
                    break;
                case "add":
                    economyAdd(sender, args);
                    break;
                case "remove":
                    economyRemove(sender, args);
                    break;
                case "reset":
                    economyReset(sender, args);
                    break;
                default:
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                    break;
            }
        } else {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return Stream.of("set", "add", "remove", "reset").filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 2) {
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
        return Collections.emptyList();
    }

    /*
    /economy set <player> <money>
     */
    private void economySet(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ECONOMY_SET))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ECONOMY_SET)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        BigDecimal money = new BigDecimal(args[2]);
        economy.setMoney(target, money);
        // TODO: Message
    }

    /*
    /economy add <player> <money>
     */
    private void economyAdd(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ECONOMY_ADD))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ECONOMY_ADD)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        BigDecimal money = new BigDecimal(args[2]);
        economy.addMoney(target, money);
        // TODO: Message
    }

    /*
    /economy remove <player> <money>
     */
    private void economyRemove(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ECONOMY_REMOVE))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ECONOMY_REMOVE)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        BigDecimal money = new BigDecimal(args[2]);
        economy.removeMoney(target, money);
        // TODO: Message
    }

    /*
    /economy reset <player>
     */
    private void economyReset(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ECONOMY_RESET))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ECONOMY_RESET)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        economy.resetMoney(target);
        // TODO: Message
    }
}
