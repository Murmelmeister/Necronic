package de.murmelmeister.citybuild.command.commands.shop.item;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.economy.EconomyProviderImpl;
import de.murmelmeister.citybuild.api.shop.item.ShopItemImpl;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class ShopItemCommand extends CommandManager {
    public ShopItemCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!isEnable(sender, Configs.COMMAND_ENABLE_SHOP_ITEM)) return true;
        if (!hasPermission(sender, Configs.PERMISSION_SHOP_ITEM)) return true;

        // TODO: Remove this
        if (args.length == 1 && args[0].equals("default")) {
            customItems.addDefaultMaterials();
            return true;
        }

        if (args.length == 1 && args[0].equals("import")) {
            shopItem.importCSV(logger, config);
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_IMPORT).replace("[FILE]", config.getString(Configs.IMPORT_DATA_SHOP_ITEMS)));
            return true;
        }

        if (args.length < 3) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        // TODO: UUID check
        UUID categoryId = UUID.fromString(args[1]);
        UUID itemId = UUID.fromString(args[2]);
        switch (args[0]) {
            case "add" -> addItem(sender, command, categoryId, itemId, args);
            case "remove" -> removeItem(sender, categoryId, itemId);
            //case "edit" -> editItem(sender, command, categoryId, itemId, args);
            default ->
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return tabComplete(Arrays.asList("add", "remove", "edit", "import"), args);
        if (args.length == 2)
            return tabComplete(shopCategory.getCategories().stream().map(UUID::toString).toList(), args);
        if (args.length == 3 && args[0].equals("add"))
            return tabComplete(customItems.getCustomItemIds().stream().map(UUID::toString).toList(), args);
        if (args.length == 3 && (args[0].equals("remove") || args[0].equals("edit"))) {
            UUID categoryId = UUID.fromString(args[1]);
            if (!shopCategory.containsCategory(categoryId))
                return Collections.emptyList();
            return tabComplete(shopItem.getCategoryItems(categoryId).stream().map(UUID::toString).toList(), args);
        }
        if (args.length == 4 && args[0].equals("edit"))
            return tabComplete(Arrays.asList("buy", "sell"), args);
        return Collections.emptyList();
    }

    private void addItem(CommandSender sender, Command command, UUID categoryId, UUID itemId, String[] args) {
        //          0   1            2              3          4     5
        // shopItem add <categoryId> <customItemId> <material> <buy> <sell>
        if (args.length < 5) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        if (!shopCategory.containsCategory(categoryId)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
            return;
        }

        if (shopItem.containsItem(itemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_EXIST));
            return;
        }

        String materialName = args[3];
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            sendMessage(sender, message.getString(Messages.INVALID_ITEM));
            return;
        }

        String buy = args[4];
        String sell = args[5];
        if (!EconomyProviderImpl.MONEY_PATTERN.matcher(buy).matches() || !EconomyProviderImpl.MONEY_PATTERN.matcher(sell).matches()) {
            sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
            return;
        }

        double buyPrice = Double.parseDouble(buy);
        double sellPrice = Double.parseDouble(sell);

        shopItem.addItem(new ShopItemImpl(UUID.randomUUID(), itemId, categoryId, material.name(), buyPrice, sellPrice));
        sendMessage(sender, message.getString(Messages.SHOP_ITEM_ADD));
    }

    // TODO: Change this method
    private void removeItem(CommandSender sender, UUID categoryId, UUID itemId) {
        //          0      1            2
        // shopItem remove <categoryId> <itemId>
        if (!shopCategory.containsCategory(categoryId)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
            return;
        }

        if (!shopItem.containsItem(itemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        shopItem.removeItem(itemId);
        sendMessage(sender, message.getString(Messages.SHOP_ITEM_REMOVE));
    }

    // TODO: Fix this method
    /*private void editItem(CommandSender sender, Command command, UUID categoryId, UUID itemId, String[] args) {
        //          0    1            2        3          4
        // shopItem edit <categoryId> <itemId> <buy|sell> <value>
        if (!shopCategory.containsCategory(categoryId)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
            return;
        }

        if (!shopItem.containsItem(itemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        switch (args[3]) {
            case "buy" -> {
                String buy = args[4];
                if (!EconomyProviderImpl.MONEY_PATTERN.matcher(buy).matches()) {
                    sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
                    return;
                }

                double buyPrice = Double.parseDouble(buy);
                double sell = shopItem.getSellPrice(itemId);
                shopItem.updateItem(itemId, categoryId, buyPrice, sell);
                sendMessage(sender, message.getString(Messages.SHOP_ITEM_EDIT));
            }
            case "sell" -> {
                String sell = args[4];
                if (!EconomyProviderImpl.MONEY_PATTERN.matcher(sell).matches()) {
                    sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
                    return;
                }

                double sellPrice = Double.parseDouble(sell);
                double buy = shopItem.getBuyPrice(itemId);
                shopItem.updateItem(itemId, categoryId, buy, sellPrice);
                sendMessage(sender, message.getString(Messages.SHOP_ITEM_EDIT));
            }
            default ->
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
    }*/
}
