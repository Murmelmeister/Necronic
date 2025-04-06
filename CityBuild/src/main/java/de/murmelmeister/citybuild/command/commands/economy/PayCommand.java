package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.economy.EconomyProvider;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class PayCommand extends CommandManager {
    public PayCommand(CityBuild plugin) {
        super(plugin);
    }

    /*
    /pay <player> <money>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_PAY))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_PAY_USE))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        int userId = user.getId(player.getUniqueId());

        if (args.length != 2) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        String username = args[0];
        String amount = args[1];

        if (!EconomyProvider.MONEY_PATTERN.matcher(amount).matches()) {
            sendMessage(player, message.getString(Messages.INVALID_NUMBERS));
            return true;
        }

        double money = Double.parseDouble(amount);

        if (username.equals("*")) {
            if (!(hasPermission(sender, Configs.PERMISSION_PAY_ALL_PLAYERS))) return true;

            if (amount.equals("*")) {
                sendMessage(player, message.getString(Messages.COMMAND_PAY_ALL));
                return true;
            }

            payAllPlayers(player, userId, money);
            return true;
        }

        Player target = sender.getServer().getPlayer(username);
        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", username));
            return true;
        }

        int targetId = user.getId(target.getUniqueId());

        if (targetId == -2) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", username));
            return true;
        }

        if (args[1].equals("*")) {
            payAllMoney(player, target, userId, targetId);
            return true;
        }

        payPlayerMoney(player, target, userId, targetId, money);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabCompletePlayers(sender, args, 1);
    }

    private void payAllPlayers(Player player, int userId, double money) {
        if (!(economy.hasEnoughMoney(userId, money))) {
            sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH));
            return;
        }

        for (Player all : player.getServer().getOnlinePlayers()) {
            if (all.equals(player)) continue;
            int allIds = user.getId(all.getUniqueId());
            if (allIds == -2) continue;
            if (economy.checkAndTransferMoney(userId, allIds, money)) {
                sendMessage(all, message.getString(Messages.COMMAND_PAY_TARGET)
                        .replace("[PLAYER]", player.getName())
                        .replace("[MONEY]", decimalFormat.format(money)));
            } else sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH));
        }

        sendMessage(player, message.getString(Messages.COMMAND_PAY_PLAYER).replace("[PLAYER]", "all").replace("[MONEY]", decimalFormat.format(money)));
    }

    private void payAllMoney(Player player, Player target, int userId, int targetId) {
        if (!(hasPermission(player, Configs.PERMISSION_PAY_ALL_MONEY))) return;

        double allMoney = economy.getMoney(userId);
        economy.transferMoney(userId, targetId, allMoney);

        sendMessage(target, message.getString(Messages.COMMAND_PAY_TARGET).replace("[PLAYER]", player.getName()).replace("[MONEY]", decimalFormat.format(allMoney)));
        sendMessage(player, message.getString(Messages.COMMAND_PAY_PLAYER).replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(allMoney)));
    }

    private void payPlayerMoney(Player player, Player target, int userId, int targetId, double money) {
        if (economy.checkAndTransferMoney(userId, targetId, money)) {
            sendMessage(target, message.getString(Messages.COMMAND_PAY_TARGET).replace("[PLAYER]", player.getName()).replace("[MONEY]", decimalFormat.format(money)));
            sendMessage(player, message.getString(Messages.COMMAND_PAY_PLAYER).replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)));
        } else sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH));
    }
}
