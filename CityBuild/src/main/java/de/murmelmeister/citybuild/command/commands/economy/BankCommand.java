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

import java.util.Arrays;
import java.util.List;

public final class BankCommand extends CommandManager {
    public BankCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!isEnable(sender, Configs.COMMAND_ENABLE_BANK)) return true;
        if (!hasPermission(sender, Configs.PERMISSION_BANK)) return true;

        Player player = getPlayer(sender);
        if (!existPlayer(sender)) return true;

        int userId = user.getId(player.getUniqueId());

        switch (args.length) {
            case 0 -> sendMessage(player, message.getString(Messages.BANK_USAGE)
                    .replace("[MONEY]", economy.getFormattedMoney(userId, config.getString(Configs.PATTERN_DECIMAL)))
                    .replace("[BANK]", economy.getFormattedBankMoney(userId, config.getString(Configs.PATTERN_DECIMAL))));
            case 2 -> {
                String input = args[1];
                if (!EconomyProviderImpl.MONEY_PATTERN.matcher(input).matches()) {
                    sendMessage(player, message.getString(Messages.INVALID_NUMBERS));
                    return true;
                }

                double amount = Double.parseDouble(input);
                switch (args[0]) {
                    case "deposit" -> {
                        if (economy.transferMoneyToBank(userId, amount))
                            sendMessage(player, message.getString(Messages.BANK_DEPOSIT).replace("[MONEY]", decimalFormat.format(amount)));
                        else sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH));
                    }
                    case "withdraw" -> {
                        if (economy.transferBankMoneyToPlayer(userId, amount))
                            sendMessage(player, message.getString(Messages.BANK_WITHDRAW).replace("[MONEY]", decimalFormat.format(amount)));
                        else sendMessage(player, message.getString(Messages.COMMAND_PAY_MONEY_ENOUGH));
                    }
                    default ->
                            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                }
            }
            default ->
                    sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return tabComplete(Arrays.asList("deposit", "withdraw"), args, 1);
    }
}
