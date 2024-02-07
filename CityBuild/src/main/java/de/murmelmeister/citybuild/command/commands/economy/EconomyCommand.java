package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EconomyCommand extends CommandManager {
    public EconomyCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ECONOMY_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_ECONOMY_COMMAND))) return true;

        if (args.length >= 2) {
            switch (args[0]) {
                case "set" -> economySet(sender, args);
                case "add" -> economyAdd(sender, args);
                case "remove" -> economyRemove(sender, args);
                case "reset" -> economyReset(sender, args);
                default ->
                        sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            }
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return tabComplete(Arrays.asList("set", "add", "remove", "reset"), args);
        if (args.length == 2) return tabCompleteOfflinePlayers(sender, args);
        return Collections.emptyList();
    }

    /*
    /economy set <player> <money>
     */
    private void economySet(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ECONOMY_SET))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_ECONOMY_SET))) return;

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
            if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                try {
                    BigDecimal money = new BigDecimal(args[2]);
                    economy.setUsername(offlinePlayer.getUniqueId(), args[1]);
                    economy.setMoney(offlinePlayer.getUniqueId(), money);
                    sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_SET)
                            .replace("[PLAYER]", args[1]).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                    sendMessage(sender, message.getString(Messages.NO_NUMBER));
                }
            } else sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        try {
            BigDecimal money = new BigDecimal(args[2]);
            economy.setMoney(target.getUniqueId(), money);
            sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_SET)
                    .replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            sendMessage(sender, message.getString(Messages.NO_NUMBER));
        }
    }

    /*
    /economy add <player> <money>
     */
    private void economyAdd(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ECONOMY_ADD))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_ECONOMY_ADD))) return;

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
            if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                try {
                    BigDecimal money = new BigDecimal(args[2]);
                    economy.setUsername(offlinePlayer.getUniqueId(), args[1]);
                    economy.addMoney(offlinePlayer.getUniqueId(), money);
                    sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_ADD)
                            .replace("[PLAYER]", args[1]).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                    sendMessage(sender, message.getString(Messages.NO_NUMBER));
                }
            } else sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        try {
            BigDecimal money = new BigDecimal(args[2]);
            economy.addMoney(target.getUniqueId(), money);
            sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_ADD)
                    .replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            sendMessage(sender, message.getString(Messages.NO_NUMBER));
        }
    }

    /*
    /economy remove <player> <money>
     */
    private void economyRemove(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ECONOMY_REMOVE))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_ECONOMY_REMOVE))) return;

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
            if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                try {
                    BigDecimal money = new BigDecimal(args[2]);
                    economy.setUsername(offlinePlayer.getUniqueId(), args[1]);
                    economy.removeMoney(offlinePlayer.getUniqueId(), money);
                    sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_REMOVE)
                            .replace("[PLAYER]", args[1]).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                    sendMessage(sender, message.getString(Messages.NO_NUMBER));
                }
            } else sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        try {
            BigDecimal money = new BigDecimal(args[2]);
            economy.removeMoney(target.getUniqueId(), money);
            sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_REMOVE)
                    .replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            sendMessage(sender, message.getString(Messages.NO_NUMBER));
        }
    }

    /*
    /economy reset <player>
     */
    private void economyReset(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ECONOMY_RESET))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_ECONOMY_RESET))) return;

        Player target = sender.getServer().getPlayer(args[1]);

        if (target == null) {
            OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
            if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                economy.setUsername(offlinePlayer.getUniqueId(), args[1]);
                economy.resetMoney(offlinePlayer.getUniqueId());
                sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_RESET).replace("[PLAYER]", args[1]));
            } else sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
            return;
        }

        economy.resetMoney(target.getUniqueId());
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_RESET).replace("[PLAYER]", target.getName()));
    }
}
