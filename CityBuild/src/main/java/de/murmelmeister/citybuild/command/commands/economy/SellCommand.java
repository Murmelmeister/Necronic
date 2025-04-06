package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.CityBuild;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class SellCommand extends CommandManager {
    public SellCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_SELL))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_SELL))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        /*ItemStack mainItem = player.getInventory().getItemInMainHand();
        if (mainItem.getType() == Material.AIR) {
            sendMessage(player, message.getString(Messages.INVALID_ITEM));
            return true;
        }

        UUID itemId = UUID.fromString(mainItem.getType().name()); // TODO: Fix that the item has a key!
        if (!customItems.containsCustomItem(itemId)) {
            sendMessage(player, message.getString(Messages.INVALID_ITEM));
            return true;
        }

        // TODO: Fix this
        ShopItem item = shopItem.getItem(itemId);
        if (!shopItem.existsItem(itemId) || item == null) {
            sendMessage(player, message.getString(Messages.INVALID_ITEM));
            return true;
        }

        double sellPrice = shopItem.getSellPrice(itemId);
        String displayName = customItems.getCustomItem(itemId).getDisplayName();

        switch (args[0]) {
            case "show" -> {
                sendMessage(player, message.getString(Messages.COMMAND_SELL_SHOW_ONE)
                        .replace("[MONEY]", decimalFormat.format(sellPrice))
                        .replace("[ITEM]", displayName));
                sendMessage(player, message.getString(Messages.COMMAND_SELL_SHOW_STACK)
                        .replace("[MONEY]", decimalFormat.format(sellPrice * mainItem.getAmount()))
                        .replace("[AMOUNT]", mainItem.getAmount() + "")
                        .replace("[ITEM]", displayName));

                int amount = 0;
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack != null && itemStack.isSimilar(mainItem)) {
                        amount += itemStack.getAmount();
                    }
                }
                sendMessage(player, message.getString(Messages.COMMAND_SELL_SHOW_INVENTORY)
                        .replace("[MONEY]", decimalFormat.format(sellPrice * amount))
                        .replace("[AMOUNT]", amount + "")
                        .replace("[ITEM]", displayName));
            }
            case "one" -> {
                int amount = mainItem.getAmount();
                amount -= 1;
                mainItem.setAmount(amount);
                player.updateInventory();

                int userId = user.getId(player.getUniqueId());
                economy.addMoney(userId, sellPrice);
                sendMessage(player, message.getString(Messages.COMMAND_SELL_ONE)
                        .replace("[MONEY]", decimalFormat.format(sellPrice))
                        .replace("[ITEM]", displayName));
                logger.info(message.getString(Messages.COMMAND_SELL_LOGGER_ONE)
                        .replace("[PLAYER]", player.getName())
                        .replace("[ITEM]", displayName)
                        .replace("[MONEY]", decimalFormat.format(sellPrice)));
            }
            case "stack" -> {
                int amount = mainItem.getAmount();
                player.getInventory().removeItem(mainItem);
                player.updateInventory();
                int userId = user.getId(player.getUniqueId());
                economy.addMoney(userId, sellPrice * amount);
                sendMessage(player, message.getString(Messages.COMMAND_SELL_STACK)
                        .replace("[MONEY]", decimalFormat.format(sellPrice * amount))
                        .replace("[AMOUNT]", amount + "")
                        .replace("[ITEM]", displayName));
                logger.info(message.getString(Messages.COMMAND_SELL_LOGGER_STACK)
                        .replace("[PLAYER]", player.getName())
                        .replace("[AMOUNT]", amount + "")
                        .replace("[ITEM]", displayName)
                        .replace("[MONEY]", decimalFormat.format(sellPrice * amount)));
            }
            case "inventory" -> {
                int amount = 0;
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack != null && itemStack.isSimilar(mainItem)) {
                        amount += itemStack.getAmount();
                        player.getInventory().removeItem(itemStack);
                    }
                }
                player.updateInventory();
                int userId = user.getId(player.getUniqueId());
                economy.addMoney(userId, sellPrice * amount);
                sendMessage(player, message.getString(Messages.COMMAND_SELL_INVENTORY)
                        .replace("[MONEY]", decimalFormat.format(sellPrice * amount))
                        .replace("[AMOUNT]", amount + "")
                        .replace("[ITEM]", displayName));
                logger.info(message.getString(Messages.COMMAND_SELL_LOGGER_INVENTORY)
                        .replace("[PLAYER]", player.getName())
                        .replace("[AMOUNT]", amount + "")
                        .replace("[ITEM]", displayName)
                        .replace("[MONEY]", decimalFormat.format(sellPrice * amount)));
            }
            default ->
                    sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }*/
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabComplete(Arrays.asList("show", "one", "stack", "inventory"), args, 1);
    }
}
