package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.economy.EconomyProviderImpl;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class EconomyCommand extends CommandManager {
    public EconomyCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ECONOMY))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_ECONOMY))) return true;

        if (args.length >= 2) {
            String username = args[1];
            String amount = args[2];

            if (!EconomyProviderImpl.MONEY_PATTERN.matcher(amount).matches()) {
                sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
                return true;
            }

            double money = Double.parseDouble(amount);

            if (username.equals("*")) {
                for (Player all : sender.getServer().getOnlinePlayers()) {
                    int allIds = user.getId(all.getUniqueId());
                    if (allIds == -2) continue;
                    switch (args[0]) {
                        case "set" -> economySet(sender, allIds, all.getName(), money);
                        case "add" -> economyAdd(sender, allIds, all.getName(), money);
                        case "remove" -> economyRemove(sender, allIds, all.getName(), money);
                        case "reset" -> economyReset(sender, allIds, all.getName());
                        default ->
                                sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                    }
                }
                return true;
            }

            getUUIDAsync(sender, username).thenAccept(uuid -> {
                if (uuid == null) return;
                if (!existUser(sender, uuid, username)) return;
                int targetId = user.getId(uuid);

                switch (args[0]) {
                    case "set" -> economySet(sender, targetId, username, money);
                    case "add" -> economyAdd(sender, targetId, username, money);
                    case "remove" -> economyRemove(sender, targetId, username, money);
                    case "reset" -> economyReset(sender, targetId, username);
                    default ->
                            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                }
            });
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return tabComplete(Arrays.asList("set", "add", "remove", "reset"), args);
        if (args.length == 2) return tabComplete(user.getUsernames(), args, 2);
        return Collections.emptyList();
    }

    private void economySet(CommandSender sender, int targetId, String username, double money) {
        economy.setMoney(targetId, money);
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_SET)
                .replace("[PLAYER]", username)
                .replace("[MONEY]", decimalFormat.format(money)));
    }

    private void economyAdd(CommandSender sender, int targetId, String username, double money) {
        economy.addMoney(targetId, money);
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_ADD)
                .replace("[PLAYER]", username)
                .replace("[MONEY]", decimalFormat.format(money)));
    }

    private void economyRemove(CommandSender sender, int targetId, String username, double money) {
        economy.removeMoney(targetId, money);
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_REMOVE)
                .replace("[PLAYER]", username)
                .replace("[MONEY]", decimalFormat.format(money)));
    }

    private void economyReset(CommandSender sender, int targetId, String username) {
        economy.resetMoney(targetId, config.getDouble(Configs.ECONOMY_DEFAULT_MONEY));
        sendMessage(sender, message.getString(Messages.COMMAND_ECONOMY_RESET).replace("[PLAYER]", username));
    }
}
