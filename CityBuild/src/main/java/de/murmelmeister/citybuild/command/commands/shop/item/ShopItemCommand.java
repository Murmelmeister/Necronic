package de.murmelmeister.citybuild.command.commands.shop.item;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.economy.EconomyProvider;
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
import java.util.stream.Collectors;

public final class ShopItemCommand extends CommandManager {
    public ShopItemCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!isEnable(sender, Configs.COMMAND_ENABLE_SHOP_ITEM)) return true;
        if (!hasPermission(sender, Configs.PERMISSION_SHOP_ITEM)) return true;

        /*
         * TODO: Remove this
         *
         * 0        1
         * shopItem [0]
         * shopItem adddefault
         */
        if (args.length == 1 && args[0].equals("adddefault")) {
            customItems.addDefaultMaterials();
            return true;
        }

        /*
         * 0        1
         * shopItem [0]
         * shopItem import
         */
        if (args.length == 1 && args[0].equals("import")) {
            shopItem.importCSV(logger, config, customItems, shopCategory);
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_IMPORT).replace("[FILE]", config.getString(Configs.IMPORT_DATA_SHOP_ITEMS)));
            return true;
        }

        switch (args[0]) {
            case "add" -> addItem(sender, command, args);
            case "remove" -> removeItem(sender, command, args);
            case "edit" -> editItem(sender, command, args);
            default ->
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return tabComplete(Arrays.asList("add", "remove", "edit", "import", "adddefault"), args);
        if (args.length == 2)
            return tabComplete(customItems.getInternNames(), args);
        if (args.length == 3)
            return tabComplete(shopCategory.getInternNames(), args);
        if (args.length == 4 && args[0].equals("add"))
            return tabComplete(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()), args);
        if (args.length == 4 && args[0].equals("edit"))
            return tabComplete(Arrays.asList("buy", "sell", "icon"), args);
        if (args.length == 5 && args[0].equals("edit") && args[3].equals("icon"))
            return tabComplete(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()), args);
        if (args.length == 5 && args[0].equals("edit") && args[3].equals("category"))
            return tabComplete(shopCategory.getInternNames(), args);
        return Collections.emptyList();
    }

    /*
     * 0        1   2            3              4          5     6
     * shopItem [0] [1]          [2]            [3]        [4]   [5]
     * shopItem add <customItemId> <categoryId> <material> <buy> <sell>
     */
    private void addItem(CommandSender sender, Command command, String[] args) {
        if (args.length < 5) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        String internNameItem = args[1];
        UUID customItemId = customItems.getCustomItemId(internNameItem);
        if (!customItems.existsItem(customItemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        String internNameCategory = args[2];
        UUID categoryId = shopCategory.getCategoryId(internNameCategory);
        if (!shopCategory.existsCategory(categoryId)) {
            sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
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
        if (!EconomyProvider.MONEY_PATTERN.matcher(buy).matches() || !EconomyProvider.MONEY_PATTERN.matcher(sell).matches()) {
            sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
            return;
        }

        double buyPrice = Double.parseDouble(buy);
        double sellPrice = Double.parseDouble(sell);

        shopItem.addItem(customItemId, categoryId, material, buyPrice, sellPrice);
        sendMessage(sender, message.getString(Messages.SHOP_ITEM_ADD));
    }

    /*
     * 0        1      2
     * shopItem [0]    [1]
     * shopItem remove <customItemId>
     */
    private void removeItem(CommandSender sender, Command command, String[] args) {
        if (args.length < 2) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        String internNameItem = args[1];
        UUID customItemId = customItems.getCustomItemId(internNameItem);
        if (!customItems.existsItem(customItemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        UUID itemId = shopItem.getItemId(customItemId);
        if (!shopItem.existsItem(itemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        shopItem.removeItem(itemId);
        sendMessage(sender, message.getString(Messages.SHOP_ITEM_REMOVE));
    }

    /*
     * 0        1    2              3                        4
     * shopItem [0]  [1]            [2]                      [3]
     * shopItem edit <customItemId> <buy|sell|icon|category> <value>
     */
    private void editItem(CommandSender sender, Command command, String[] args) {
        if (args.length < 5) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        String internNameItem = args[1];
        UUID customItemId = customItems.getCustomItemId(internNameItem);
        if (!customItems.existsItem(customItemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        UUID itemId = shopItem.getItemId(customItemId);
        if (!shopItem.existsItem(itemId)) {
            sendMessage(sender, message.getString(Messages.SHOP_ITEM_NOT_EXIST));
            return;
        }

        switch (args[2]) {
            case "buy" -> {
                String buy = args[3];
                if (!EconomyProvider.MONEY_PATTERN.matcher(buy).matches()) {
                    sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
                    return;
                }

                double buyPrice = Double.parseDouble(buy);
                shopItem.updateBuyPrice(itemId, buyPrice);
                sendMessage(sender, message.getString(Messages.SHOP_ITEM_EDIT));
            }
            case "sell" -> {
                String sell = args[3];
                if (!EconomyProvider.MONEY_PATTERN.matcher(sell).matches()) {
                    sendMessage(sender, message.getString(Messages.INVALID_NUMBERS));
                    return;
                }

                double sellPrice = Double.parseDouble(sell);
                shopItem.updateSellPrice(itemId, sellPrice);
                sendMessage(sender, message.getString(Messages.SHOP_ITEM_EDIT));
            }
            case "icon" -> {
                String materialName = args[3];
                Material material = Material.getMaterial(materialName);
                if (material == null) {
                    sendMessage(sender, message.getString(Messages.INVALID_ITEM));
                    return;
                }

                shopItem.updateIcon(itemId, material);
                sendMessage(sender, message.getString(Messages.SHOP_ITEM_EDIT));
            }
            case "category" -> {
                String categoryName = args[3];
                UUID categoryId = shopCategory.getCategoryId(categoryName);
                if (!shopCategory.existsCategory(categoryId)) {
                    sendMessage(sender, message.getString(Messages.SHOP_CATEGORY_NOT_EXIST));
                    return;
                }

                shopItem.updateCategory(itemId, categoryId);
                sendMessage(sender, message.getString(Messages.SHOP_ITEM_EDIT));
            }
            default ->
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
    }
}
