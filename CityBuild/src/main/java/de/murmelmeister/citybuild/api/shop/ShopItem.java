package de.murmelmeister.citybuild.api.shop;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.CustomItems;
import de.murmelmeister.citybuild.api.Economy;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.files.MessageFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import de.murmelmeister.murmelapi.database.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.slf4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class ShopItem {
    private static final String TABLE_NAME = "CB_ShopItems";
    private final Database database;
    private final CustomItems customItems;

    public ShopItem(Database database, CustomItems customItems) {
        this.database = database;
        this.customItems = customItems;
        createTable();
        Procedure.loadAll(database);
    }

    /*
     * ItemId soll zu mehreren Kategorien gehören können
     */
    private void createTable() {
        database.createTable(TABLE_NAME, "ItemID VARCHAR(255) PRIMARY KEY, CategoryID VARCHAR(255), BuyPrice DOUBLE, SellPrice DOUBLE");
    }

    public boolean existItem(String itemId) {
        return database.exists(Procedure.ITEMS_GET.getName(), itemId);
    }

    public void addItem(String itemId, String categoryId, double buy, double sell) {
        if (itemId.length() > 255)
            throw new IllegalArgumentException("ItemID is too long. Max length is 255 characters");
        if (categoryId.length() > 255)
            throw new IllegalArgumentException("CategoryID is too long. Max length is 255 characters");
        database.callUpdate(Procedure.ITEMS_CREATE.getName(), itemId, categoryId, buy, sell);
    }

    public void importCSV(Logger logger, ConfigFile config) {
        String filePath = CityBuild.getMainPath() + config.getString(Configs.IMPORT_PATH) + config.getString(Configs.IMPORT_DATA_SHOP_ITEMS);
        File file = new File(filePath);
        logger.info("Importing items from file: {}", file.getName());
        if (!file.exists()) {
            logger.error("File not found: {}", file.getName());
            return;
        }

        logger.info("Reading file: {}", file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String itemId = data[0].trim();
                    String categoryId = data[1].trim();
                    double buy = Double.parseDouble(data[2].trim());
                    double sell = Double.parseDouble(data[3].trim());
                    if (existItem(itemId)) updateItem(itemId, categoryId, buy, sell);
                    else addItem(itemId, categoryId, buy, sell);
                }
            }
        } catch (IOException e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
        logger.info("Importing items from file: {} finished", file.getName());
    }

    public void removeItem(String itemId) {
        database.callUpdate(Procedure.ITEMS_DELETE.getName(), itemId);
    }

    public void removeCategoryItems(String categoryId) {
        database.callUpdate(Procedure.ITEMS_DELETE_CATEGORY_ITEMS.getName(), categoryId);
    }

    public double getBuyPrice(String itemId) {
        return database.query(0.0D, "BuyPrice", double.class, Procedure.ITEMS_GET.getName(), itemId);
    }

    public double getSellPrice(String itemId) {
        return database.query(0.0D, "SellPrice", double.class, Procedure.ITEMS_GET.getName(), itemId);
    }

    public Material getMaterial(String itemId) {
        return !existItem(itemId) ? null : customItems.getMaterial(itemId);
    }

    public ItemStack getItemStack(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    public ItemStack getIcon(ConfigFile configFile, MessageFile messageFile, Economy economy, int userId, String itemId) {
        Material material = getMaterial(itemId);
        if (material == null) return null;
        DecimalFormat decimalFormat = new DecimalFormat(configFile.getString(Configs.PATTERN_DECIMAL));
        int maxStackSize = material.getMaxStackSize();
        double buy = getBuyPrice(itemId);
        double sell = getSellPrice(itemId);
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(CityBuild.getKeyShopItem(), PersistentDataType.BOOLEAN, true);

        itemMeta.displayName(MiniMessage.miniMessage().deserialize(customItems.getDisplayName(itemId)));

        List<Component> lore = new ArrayList<>();
        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_BUY_ONE).replace("[PRICE]", decimalFormat.format(buy))));

        if (maxStackSize != 1)
            lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_BUY_STACK).replace("[PRICE]", decimalFormat.format(buy * maxStackSize))));

        lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_SELL).replace("[SELL]", decimalFormat.format(sell))));

        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY).replace("[MONEY]", decimalFormat.format(economy.getMoney(userId)))));

        if (economy.hasEnoughMoney(userId, buy))
            lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY_LEFT_ITEM).replace("[MONEY]", decimalFormat.format(economy.getMoney(userId) - buy))));
        else
            lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH_ITEM)));

        if (maxStackSize != 1)
            if (economy.hasEnoughMoney(userId, buy * maxStackSize))
                lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY_LEFT_STACK).replace("[MONEY]", decimalFormat.format(economy.getMoney(userId) - buy * maxStackSize))));
            else
                lore.add(MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH_STACK)));

        itemMeta.lore(lore);
        for (ItemFlag flag : ItemFlag.values())
            itemMeta.addItemFlags(flag);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public List<String> getCategoryItems(String categoryId) {
        return database.queryList(new ArrayList<>(), "ItemID", String.class, Procedure.ITEMS_GET_CATEGORY_ITEMS.getName(), categoryId);
    }

    public void updateItem(String itemId, String categoryId, double buy, double sell) {
        if (itemId.length() > 255)
            throw new IllegalArgumentException("ItemID is too long. Max length is 255 characters");
        if (categoryId.length() > 255)
            throw new IllegalArgumentException("CategoryID is too long. Max length is 255 characters");
        database.callUpdate(Procedure.ITEMS_UPDATE.getName(), itemId, categoryId, buy, sell);
    }

    private enum Procedure {
        ITEMS_CREATE("Shop_Items_Create", "item VARCHAR(255), cid VARCHAR(255), buy DOUBLE, sell DOUBLE",
                "INSERT INTO [TABLE] VALUES (item, cid, buy, sell);"),
        ITEMS_DELETE("Shop_Items_Delete", "item VARCHAR(255)", "DELETE FROM [TABLE] WHERE ItemID=item;"),
        ITEMS_DELETE_CATEGORY_ITEMS("Shop_Items_DeleteCategoryItems", "cid VARCHAR(255)", "DELETE FROM [TABLE] WHERE CategoryID=cid;"),
        ITEMS_GET("Shop_Items_Get", "item VARCHAR(255)", "SELECT * FROM [TABLE] WHERE ItemID=item;"),
        ITEMS_GET_CATEGORY_ITEMS("Shop_Items_GetCategoryItems", "cid VARCHAR(255)", "SELECT * FROM [TABLE] WHERE CategoryID=cid;"),
        ITEMS_UPDATE("Shop_Items_Update", "item VARCHAR(255), cid VARCHAR(255), buy DOUBLE, sell DOUBLE",
                "UPDATE [TABLE] SET CategoryID=cid, BuyPrice=buy, SellPrice=sell WHERE ItemID=item;");
        private static final Procedure[] VALUES = values();

        private final String name;
        private final String query;

        Procedure(final String name, final String input, final String query) {
            this.name = name;
            this.query = Database.getProcedureQuery(name, input, query);
        }

        public String getName() {
            return name;
        }

        public String getQuery() {
            return query.replace("[TABLE]", TABLE_NAME);
        }

        private static void loadAll(Database database) {
            for (Procedure procedure : VALUES) database.update(procedure.getQuery());
        }
    }
}
