package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SellCommand extends CommandManager {
    public SellCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_SELL))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_SELL)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        // TODO: Edit the sell list

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        ItemStack itemStack = player.getItemInHand();
        if (itemStack.getType().equals(Material.AIR)) {
            sendMessage(player, message.getString(Messages.COMMAND_SELL_AIR));
            return true;
        }

        switch (args[0]) {
            case "hand":
                sendMessage(player, message.getString(Messages.COMMAND_SELL_USE).replace("[MONEY]", decimalFormat.format(itemValue.sellItem(player, itemStack)))
                        .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[ITEM]", itemStack.getType().name()));
                break;
            case "price":
                BigDecimal price = BigDecimal.valueOf(itemValue.getValue(itemStack.getType()));
                BigDecimal result = price.multiply(BigDecimal.valueOf(itemStack.getAmount()));
                if (config.getBoolean(Configs.MATERIAL_CASE))
                    sendMessage(player, message.getString(Messages.COMMAND_SELL_PRICE).replace("[ITEM]", itemStack.getType().name().toLowerCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(itemStack.getType())))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[PREFIX]", message.prefix()).replace("[AMOUNT]", decimalFormat.format(itemStack.getAmount())).replace("[VALUE]", decimalFormat.format(result)));
                else
                    sendMessage(player, message.getString(Messages.COMMAND_SELL_PRICE).replace("[ITEM]", itemStack.getType().name().toUpperCase()).replace("[MONEY]", decimalFormat.format(itemValue.getValue(itemStack.getType())))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[PREFIX]", message.prefix()).replace("[AMOUNT]", decimalFormat.format(itemStack.getAmount())).replace("[VALUE]", decimalFormat.format(result)));

                break;
            default:
                sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabComplete(Arrays.asList("hand", "price"), args, 1);
    }
}
