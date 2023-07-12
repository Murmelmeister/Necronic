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
import java.util.List;

public class PayCommand extends CommandManager {
    public PayCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_PAY))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_PAY_USE)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        if (args.length != 2) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        if (args[0].equals("*")) {
            if (!(player.hasPermission(config.getString(Configs.PERMISSION_PAY_ALL_PLAYERS)))) {
                sendMessage(player, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            if (args[1].equals("*")) {
                sendMessage(player, message.getString(Messages.COMMAND_PAY_ALL));
                return true;
            }

            payAllPlayers(player, args);
            return true;
        }

        Player target = sender.getServer().getPlayer(args[0]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return true;
        }

        if (args[1].equals("*")) {
            payAllMoney(player, target);
            return true;
        }

        payPlayerMoney(player, target, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    private void payAllPlayers(Player player, String[] args) {
        BigDecimal money = new BigDecimal(args[1]);

        if (!(player.hasPermission(config.getString(Configs.PERMISSION_PAY_NEGATIVE))) && -money.doubleValue() >= 0) {
            sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_NEGATIVE));
            return;
        }

        if (!(economy.hasEnoughMoney(player, money.doubleValue()))) {
            sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
            return;
        }

        player.getServer().getOnlinePlayers().forEach(all -> {
            economy.payMoney(player, all, money);
            sendMessage(all, message.getString(Messages.COMMAND_PAY_TARGET).replace("[PLAYER]", player.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        });
        sendMessage(player, message.getString(Messages.COMMAND_PAY_PLAYER).replace("[PLAYER]", "all").replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
    }

    private void payAllMoney(Player player, Player target) {
        if (!(player.hasPermission(config.getString(Configs.PERMISSION_PAY_ALL_MONEY)))) {
            sendMessage(player, message.getString(Messages.NO_PERMISSION));
            return;
        }

        BigDecimal allMoney = economy.getMoney(player);

        if (!(player.hasPermission(config.getString(Configs.PERMISSION_PAY_NEGATIVE))) && -allMoney.doubleValue() >= 0) {
            sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_NEGATIVE));
            return;
        }

        economy.payMoney(player, target, allMoney);
        sendMessage(target, message.getString(Messages.COMMAND_PAY_TARGET).replace("[PLAYER]", player.getName()).replace("[MONEY]", decimalFormat.format(allMoney)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        sendMessage(player, message.getString(Messages.COMMAND_PAY_PLAYER).replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(allMoney)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
    }

    private void payPlayerMoney(Player player, Player target, String[] args) {
        BigDecimal money = new BigDecimal(args[1]);

        if (!(player.hasPermission(config.getString(Configs.PERMISSION_PAY_NEGATIVE))) && -money.doubleValue() >= 0) {
            sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_NEGATIVE));
            return;
        }

        if (!(economy.hasEnoughMoney(player, money.doubleValue()))) {
            sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
            return;
        }

        economy.payMoney(player, target, money);
        sendMessage(target, message.getString(Messages.COMMAND_PAY_TARGET).replace("[PLAYER]", player.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
        sendMessage(player, message.getString(Messages.COMMAND_PAY_PLAYER).replace("[PLAYER]", target.getName()).replace("[MONEY]", decimalFormat.format(money)).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)));
    }
}
