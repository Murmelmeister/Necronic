package de.murmelmeister.citybuild.api.shop.category;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.database.Database;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ShopCategoryProviderImpl implements ShopCategoryProvider {
    private static final String TABLE_NAME = "CB_ShopCategory";
    private final Database database;

    private final Map<UUID, ShopCategory> cache = new ConcurrentHashMap<>();

    public ShopCategoryProviderImpl(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
        loadData();
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ID UUID PRIMARY KEY, DisplayName TINYTEXT, Icon TINYTEXT, Description MEDIUMTEXT");
    }

    private void loadData() {
        cache.clear();
        database.queryProcess(resultSet -> {
            UUID id = UUID.fromString(resultSet.getString("ID"));
            String displayName = resultSet.getString("DisplayName");
            String icon = resultSet.getString("Icon");
            String description = resultSet.getString("Description");
            ShopCategory category = new ShopCategoryImpl(id, displayName, icon, description);
            cache.put(id, category);
        }, Procedure.GET_DATA.getName());
    }

    @Override
    public ShopCategory getCategory(UUID id) {
        return cache.get(id);
    }

    @Override
    public boolean containsCategory(UUID id) {
        return cache.containsKey(id);
    }

    @Override
    public void addCategory(ShopCategory category) {
        cache.put(category.getId(), category);
        database.asyncUpdate(Procedure.ADD_CATEGORY.getName(), category.getId(), category.getDisplayName(), category.getIcon().name(), category.getDescription());
    }

    @Override
    public void removeCategory(UUID id) {
        cache.remove(id);
        database.asyncUpdate(Procedure.REMOVE_CATEGORY.getName(), id);
    }

    @Override
    public void updateCategory(ShopCategory category) {
        cache.put(category.getId(), category);
        database.asyncUpdate(Procedure.UPDATE_CATEGORY.getName(), category.getId(), category.getDisplayName(), category.getIcon().name(), category.getDescription());
    }

    @Override
    public List<UUID> getCategories() {
        return cache.keySet().stream().toList();
    }

    @Override
    public void importCSV(Logger logger, ConfigFile config) {
        String filePath = CityBuild.getMainPath() + config.getString(Configs.IMPORT_PATH) + config.getString(Configs.IMPORT_DATA_SHOP_CATEGORIES);
        File file = new File(filePath);
        logger.info("Importing categories from file: {}", file.getName());
        if (!file.exists()) {
            logger.error("File not found: {}", file.getName());
            return;
        }

        logger.info("Reading file: {}", file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 4) {
                    logger.error("Invalid line: {}", line);
                    continue;
                }
                UUID id = UUID.fromString(data[0]);
                if (containsCategory(id)) {
                    logger.warn("ShopCategory with ID {} already exists, skipping...", id);
                    continue;
                }
                String displayName = data[1];
                String icon = data[2];
                String description = data[3];
                ShopCategory category = new ShopCategoryImpl(id, displayName, icon, description);
                addCategory(category);
            }
        } catch (IOException e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
        logger.info("Importing categories from file: {} finished", file.getName());
    }

    private enum Procedure {
        ADD_CATEGORY("CB_ShopCategory_AddCategory", "uid UUID, display TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "INSERT INTO [TABLE] VALUES (uid,display,mat,des);"),
        REMOVE_CATEGORY("CB_ShopCategory_RemoveCategory", "uid UUID", "DELETE FROM [TABLE] WHERE ID=uid;"),
        GET_DATA("CB_ShopCategory_GetData", "", "SELECT * FROM [TABLE];"),
        UPDATE_CATEGORY("CB_ShopCategory_UpdateCategory", "uid UUID, display TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "UPDATE [TABLE] SET DisplayName=display, Icon=mat, Description=des WHERE ID=uid;");
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
