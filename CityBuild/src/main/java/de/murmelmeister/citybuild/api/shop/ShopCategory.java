package de.murmelmeister.citybuild.api.shop;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.database.Database;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ShopCategory {
    private static final String TABLE_NAME = "CB_ShopCategory";
    private final Database database;

    public ShopCategory(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "CategoryID VARCHAR(255) PRIMARY KEY, DisplayName TINYTEXT, IconItem TINYTEXT");
    }

    public boolean existCategory(String id) {
        return database.exists(Procedure.CATEGORY_GET.getName(), id);
    }

    public void addCategory(String id, String displayName, Material icon) {
        if (id.length() > 255) throw new IllegalArgumentException("CategoryID is too long. Max length is 255 characters");
        if (displayName.length() > 255) throw new IllegalArgumentException("DisplayName is too long. Max length is 255 characters");
        if (icon.name().length() > 255) throw new IllegalArgumentException("IconItem is too long. Max length is 255 characters");
        database.callUpdate(Procedure.CATEGORY_CREATE.getName(), id, displayName, icon.name());
    }

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
                if (data.length != 3) {
                    logger.error("Invalid line: {}", line);
                    continue;
                }
                String id = data[0];
                String displayName = data[1];
                Material icon = Material.getMaterial(data[2].toUpperCase());
                if (existCategory(id)) updateCategory(id, displayName, icon);
                else addCategory(id, displayName, icon);
            }
        } catch (IOException e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
        logger.info("Importing categories from file: {} finished", file.getName());
    }

    public void removeCategory(ShopItem item, String id) {
        item.removeCategoryItems(id);
        database.callUpdate(Procedure.CATEGORY_DELETE.getName(), id);
    }

    public Material getIconMaterial(String id) {
        String material = database.query(null, "IconItem", String.class, Procedure.CATEGORY_GET.getName(), id);
        return material == null ? null : Material.getMaterial(material);
    }

    public String getDisplayName(String id) {
        return database.query(null, "DisplayName", String.class, Procedure.CATEGORY_GET.getName(), id);
    }

    public ItemStack getIcon(String id) {
        Material material = getIconMaterial(id);
        if (material == null) return null;
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(CityBuild.getKeyShopCategory(), PersistentDataType.BOOLEAN, true);
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(getDisplayName(id)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public List<String> getCategories() {
        return database.queryList(new ArrayList<>(), "CategoryID", String.class, Procedure.CATEGORY_GET_ALL.getName());
    }

    public void updateCategory(String id, String displayName, Material icon) {
        if (displayName.length() > 255) throw new IllegalArgumentException("DisplayName is too long. Max length is 255 characters");
        if (icon.name().length() > 255) throw new IllegalArgumentException("IconItem is too long. Max length is 255 characters");
        database.callUpdate(Procedure.CATEGORY_UPDATE.getName(), id, displayName, icon.name());
    }

    private enum Procedure {
        CATEGORY_CREATE("Shop_Category_Create", "id VARCHAR(255), display TINYTEXT, icon TINYTEXT", "INSERT INTO [TABLE] VALUES (id, display, icon);"),
        CATEGORY_DELETE("Shop_Category_Delete", "id VARCHAR(255)", "DELETE FROM [TABLE] WHERE CategoryID=id;"),
        CATEGORY_GET("Shop_Category_Get", "id VARCHAR(255)", "SELECT * FROM [TABLE] WHERE CategoryID=id;"),
        CATEGORY_GET_ALL("Shop_Category_GetAll", "", "SELECT * FROM [TABLE];"),
        CATEGORY_UPDATE("Shop_Category_Update", "id VARCHAR(255), display TINYTEXT, icon TINYTEXT", "UPDATE [TABLE] SET DisplayName=display, IconItem=icon WHERE CategoryID=id;");
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
