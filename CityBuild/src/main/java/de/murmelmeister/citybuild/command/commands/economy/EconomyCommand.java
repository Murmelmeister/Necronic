package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;

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
            return tabComplete(Arrays.asList("set", "add", "remove", "reset"), args);
        if (args.length == 2)
            return playerTabComplete(sender, args);
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
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_SET)
                .replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
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
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_ADD)
                .replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
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
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_REMOVE)
                .replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
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
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_RESET).replace("[PLAYER]", target.getName()));
    }
}
