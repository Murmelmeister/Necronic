package de.murmelmeister.citybuild.api.shop.item;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.economy.Economy;
import de.murmelmeister.citybuild.api.item.CustomItem;
import de.murmelmeister.citybuild.api.shop.category.ShopCategory;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class ShopItemProvider implements ShopItem {
    private static final String TABLE_NAME = "CB_ShopItems";
    private final Database database;

    public ShopItemProvider(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ID UUID PRIMARY KEY, CustomItem UUID, Category UUID, " +
                                         "FOREIGN KEY (CustomItem) REFERENCES CB_CustomItems(ID), " +
                                         "FOREIGN KEY (Category) REFERENCES CB_ShopCategory(ID), " +
                                         "Icon TINYTEXT, BuyPrice DOUBLE, SellPrice DOUBLE");
    }

    @Override
    public boolean existsItem(UUID id) {
        return id != null && database.existsCallable(Procedure.GET_DATA.getName(), id.toString());
    }

    @Override
    public void addItem(UUID customItemId, UUID categoryId, Material icon, double buyPrice, double sellPrice) {
        addItem(UUID.randomUUID(), customItemId, categoryId, icon, buyPrice, sellPrice);
    }

    @Override
    public void addItem(UUID id, UUID customItemId, UUID categoryId, Material icon, double buyPrice, double sellPrice) {
        database.updateCallable(Procedure.ADD_ITEM.getName(), id.toString(), customItemId.toString(), categoryId.toString(), icon.name(), buyPrice, sellPrice);
    }

    @Override
    public void removeItem(UUID id) {
        database.updateCallable(Procedure.REMOVE_ITEM.getName(), id.toString());
    }

    @Override
    public void updateIcon(UUID id, Material icon) {
        database.updateCallable(Procedure.UPDATE_MATERIAL.getName(), id.toString(), icon.name());
    }

    @Override
    public void updateBuyPrice(UUID id, double buyPrice) {
        database.updateCallable(Procedure.UPDATE_BUY_PRICE.getName(), id.toString(), buyPrice);
    }

    @Override
    public void updateSellPrice(UUID id, double sellPrice) {
        database.updateCallable(Procedure.UPDATE_SELL_PRICE.getName(), id.toString(), sellPrice);
    }

    @Override
    public List<UUID> getCategoryItems(UUID categoryId) {
        return database.queryListCallable(Procedure.GET_ITEMS.getName(), new LinkedList<>(), resultSet -> {
            String id = resultSet.getString("ID");
            return id == null ? null : UUID.fromString(id);
        }, categoryId.toString());
    }

    @Override
    public UUID getItemId(UUID customItemId, UUID categoryId) {
        return database.queryCallable(Procedure.GET_ID_BY_OTHER.getName(), null, resultSet -> {
            String id = resultSet.getString("ID");
            return id == null ? null : UUID.fromString(id);
        }, customItemId.toString(), categoryId.toString());
    }

    @Override
    public UUID getCustomItem(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> {
            String customItem = resultSet.getString("CustomItem");
            return customItem != null ? UUID.fromString(customItem) : null;
        }, id.toString());
    }

    @Override
    public UUID getCategory(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> {
            String category = resultSet.getString("Category");
            return category != null ? UUID.fromString(category) : null;
        }, id.toString());
    }

    @Override
    public Material getIcon(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> {
            String icon = resultSet.getString("Icon");
            return icon != null ? Material.getMaterial(icon) : null;
        }, id.toString());
    }

    @Override
    public double getBuyPrice(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> resultSet.getDouble("BuyPrice"), id.toString());
    }

    @Override
    public double getSellPrice(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> resultSet.getDouble("SellPrice"), id.toString());
    }

    @Override
    public ItemStack getItemStack(ConfigFile config, MessageFile message, CustomItem customItem, Economy economy, int userId, UUID id) {
        DecimalFormat format = new DecimalFormat(config.getString(Configs.PATTERN_DECIMAL));
        Material icon = getIcon(id);
        int maxStackSize = icon.getMaxStackSize();
        UUID customItemId = getCustomItem(id);
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();

        double buyPrice = getBuyPrice(id);
        double sellPrice = getSellPrice(id);

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        //container.set(CityBuild.getKeyShopItem(), PersistentDataType.STRING, id.toString());
        container.set(CityBuild.getKeyShopItem(), PersistentDataType.BOOLEAN, true);

        itemMeta.displayName(customItem.getDisplayName(customItemId));

        List<Component> lore = new LinkedList<>();
        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_BUY_ONE)
                .replace("[PRICE]", format.format(buyPrice))));

        if (maxStackSize != 1)
            lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_BUY_STACK)
                    .replace("[PRICE]", format.format(buyPrice * maxStackSize))));

        lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_SELL)
                .replace("[SELL]", format.format(sellPrice))));

        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY)
                .replace("[MONEY]", format.format(economy.getMoney(userId)))));

        if (economy.hasEnoughMoney(userId, buyPrice))
            lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_LEFT_ITEM)
                    .replace("[MONEY]", format.format(economy.getMoney(userId) - buyPrice))));
        else
            lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH_ITEM)));

        if (maxStackSize != 1)
            if (economy.hasEnoughMoney(userId, buyPrice * maxStackSize))
                lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_LEFT_STACK)
                        .replace("[MONEY]", format.format(economy.getMoney(userId) - buyPrice * maxStackSize))));
            else
                lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH_STACK)));

        itemMeta.lore(lore);
        for (ItemFlag flag : ItemFlag.values())
            itemMeta.addItemFlags(flag);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void importCSV(Logger logger, ConfigFile config, CustomItem customItem, ShopCategory shopCategory) {
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
                if (data.length != 5) {
                    logger.error("Invalid line: {}", line);
                    continue;
                }
                UUID id = UUID.randomUUID();
                if (existsItem(id)) {
                    logger.warn("ShopItem with ID {} already exists, skipping...", id);
                    continue;
                }
                UUID item = customItem.getCustomItemId(data[0]);
                UUID category = shopCategory.getCategoryId(data[1]);
                String icon = data[2];
                double buyPrice = Double.parseDouble(data[3]);
                double sellPrice = Double.parseDouble(data[4]);
                Material material = Material.getMaterial(icon);
                if (material == null) {
                    logger.error("Invalid material: {}; Data: {}", icon, data);
                    continue;
                }
                addItem(id, item, category, material, buyPrice, sellPrice);
            }
        } catch (IOException e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
        logger.info("Importing categories from file: {} finished", file.getName());
    }

    private enum Procedure {
        ADD_ITEM("CB_ShopItem_AddItem", "sid UUID, item UUID, cat UUID, mat TINYTEXT, buy DOUBLE, sell DOUBLE",
                "INSERT INTO [TABLE] VALUES (sid,item,cat,mat,buy,sell);"),
        REMOVE_ITEM("CB_ShopItem_RemoveItem", "sid UUID", "DELETE FROM [TABLE] WHERE ID=sid;"),
        GET_DATA("CB_ShopItem_GetData", "sid UUID", "SELECT * FROM [TABLE] WHERE ID=sid;"),
        GET_ITEMS("CB_ShopItem_GetItems", "cid UUID", "SELECT * FROM [TABLE] WHERE Category=cid;"),
        GET_ID_BY_OTHER("CB_ShopItem_GetIdByOther", "item UUID, cat UUID", "SELECT ID FROM [TABLE] WHERE CustomItem=item AND Category=cat;"),
        UPDATE_MATERIAL("CB_ShopItem_UpdateMaterial", "sid UUID, mat TINYTEXT", "UPDATE [TABLE] SET Icon=mat WHERE ID=sid;"),
        UPDATE_BUY_PRICE("CB_ShopItem_UpdateBuyPrice", "sid UUID, buy DOUBLE", "UPDATE [TABLE] SET BuyPrice=buy WHERE ID=sid;"),
        UPDATE_SELL_PRICE("CB_ShopItem_UpdateSellPrice", "sid UUID, sell DOUBLE", "UPDATE [TABLE] SET SellPrice=sell WHERE ID=sid;");
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

        public static void loadAll(Database database) {
            for (Procedure procedure : VALUES) database.update(procedure.getQuery());
        }
    }
}
