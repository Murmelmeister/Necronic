package de.murmelmeister.citybuild.api.shop.item;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.database.Database;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ShopItemProviderImpl implements ShopItemProvider {
    private static final String TABLE_NAME = "CB_ShopItems";
    private final Database database;

    private final Map<UUID, ShopItem> cache = new ConcurrentHashMap<>();

    public ShopItemProviderImpl(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
        loadData();
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ID UUID PRIMARY KEY, CustomItem UUID, Category UUID, " +
                                         "FOREIGN KEY (CustomItem) REFERENCES CB_CustomItems(ID), " +
                                         "FOREIGN KEY (Category) REFERENCES CB_ShopCategory(ID), " +
                                         "Icon TINYTEXT, BuyPrice DOUBLE, SellPrice DOUBLE");
    }

    private void loadData() {
        cache.clear();
        database.queryProcess(resultSet -> {
            UUID id = UUID.fromString(resultSet.getString("ID"));
            UUID customItem = UUID.fromString(resultSet.getString("CustomItem"));
            UUID category = UUID.fromString(resultSet.getString("Category"));
            String icon = resultSet.getString("Icon");
            double buyPrice = resultSet.getDouble("BuyPrice");
            double sellPrice = resultSet.getDouble("SellPrice");
            ShopItem item = new ShopItemImpl(id, customItem, category, icon, buyPrice, sellPrice);
            cache.put(id, item);
        }, Procedure.GET_DATA.getName());
    }

    @Override
    public ShopItem getItem(UUID id) {
        return cache.get(id);
    }

    @Override
    public boolean containsItem(UUID id) {
        return cache.containsKey(id);
    }

    @Override
    public void addItem(ShopItem item) {
        cache.put(item.getId(), item);
        database.asyncUpdate(Procedure.ADD_ITEM.getName(), item.getId(), item.getCustomItem(), item.getCategory(),
                item.getIcon().name(), item.getBuyPrice(), item.getSellPrice());
    }

    @Override
    public void removeItem(UUID id) {
        cache.remove(id);
        database.asyncUpdate(Procedure.REMOVE_ITEM.getName(), id);
    }

    @Override
    public void updateItem(ShopItem item) {
        cache.put(item.getId(), item);
        database.asyncUpdate(Procedure.UPDATE_ITEM.getName(), item.getId(), item.getCustomItem(), item.getCategory(),
                item.getIcon().name(), item.getBuyPrice(), item.getSellPrice());
    }

    @Override
    public List<UUID> getCategoryItems(UUID category) {
        List<UUID> items = new ArrayList<>();
        for (ShopItem item : cache.values())
            if (item.getCategory().equals(category))
                items.add(item.getId());
        return items;
    }

    @Override
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
                if (data.length != 6) {
                    logger.error("Invalid line: {}", line);
                    continue;
                }
                UUID id = UUID.fromString(data[0]);
                if (containsItem(id)) {
                    logger.warn("ShopItem with ID {} already exists, skipping...", id);
                    continue;
                }
                UUID customItem = UUID.fromString(data[1]);
                UUID category = UUID.fromString(data[2]);
                String icon = data[3];
                double buyPrice = Double.parseDouble(data[4]);
                double sellPrice = Double.parseDouble(data[5]);
                ShopItem item = new ShopItemImpl(id, customItem, category, icon, buyPrice, sellPrice);
                addItem(item);
            }
        } catch (IOException e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
        logger.info("Importing categories from file: {} finished", file.getName());
    }

    private enum Procedure {
        ADD_ITEM("CB_ShopItem_AddItem", "uid UUID, item UUID, cat UUID, mat TINYTEXT, buy DOUBLE, sell DOUBLE",
                "INSERT INTO [TABLE] VALUES (uid,item,cat,mat,buy,sell);"),
        REMOVE_ITEM("CB_ShopItem_RemoveItem", "uid UUID", "DELETE FROM [TABLE] WHERE ID=uid;"),
        GET_DATA("CB_ShopItem_GetData", "", "SELECT * FROM [TABLE];"),
        UPDATE_ITEM("CB_ShopItem_UpdateItem", "uid UUID, item UUID, cat UUID, mat TINYTEXT, buy DOUBLE, sell DOUBLE",
                "UPDATE [TABLE] SET CustomItem=item, Category=cat, Icon=mat, BuyPrice=buy, SellPrice=sell WHERE ID=uid;");
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
