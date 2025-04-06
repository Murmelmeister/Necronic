package de.murmelmeister.citybuild.api.shop.category;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.database.Database;
import net.kyori.adventure.text.Component;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class ShopCategoryProvider implements ShopCategory {
    private static final String TABLE_NAME = "CB_ShopCategory";
    private final Database database;

    public ShopCategoryProvider(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ID UUID PRIMARY KEY, InternName TINYTEXT UNIQUE, DisplayName TINYTEXT, Icon TINYTEXT, Description MEDIUMTEXT");
    }

    @Override
    public boolean existsCategory(UUID id) {
        return database.existsCallable(Procedure.GET_DATA.getName(), id.toString());
    }

    @Override
    public boolean existsCategory(String internName) {
        return database.existsCallable(Procedure.GET_ID_BY_NAME.getName(), internName);
    }

    @Override
    public void addCategory(String internName, String displayName, Material icon) {
        addCategory(internName, displayName, icon, null);
    }

    @Override
    public void addCategory(String internName, String displayName, Material icon, String description) {
        addCategory(UUID.randomUUID(), internName, displayName, icon, description);
    }

    @Override
    public void addCategory(UUID id, String internName, String displayName, Material icon) {
        addCategory(id, internName, displayName, icon, null);
    }

    @Override
    public void addCategory(UUID id, String internName, String displayName, Material icon, String description) {
        database.updateCallable(Procedure.ADD_CATEGORY.getName(), id.toString(), internName, displayName, icon.name(), description);
    }

    @Override
    public void removeCategory(UUID id) {
        database.updateCallable(Procedure.REMOVE_CATEGORY.getName(), id.toString());
    }

    @Override
    public void updateDisplayName(UUID id, String displayName) {
        database.updateCallable(Procedure.UPDATE_DISPLAY_NAME.getName(), id.toString(), displayName);
    }

    @Override
    public void updateIcon(UUID id, Material icon) {
        database.updateCallable(Procedure.UPDATE_ICON.getName(), id.toString(), icon.name());
    }

    @Override
    public void updateDescription(UUID id, String description) {
        database.updateCallable(Procedure.UPDATE_DESCRIPTION.getName(), id.toString(), description);
    }

    @Override
    public List<UUID> getCategories() {
        return database.queryListCallable(Procedure.GET_ALL.getName(), new LinkedList<>(), resultSet -> UUID.fromString(resultSet.getString("ID")));
    }

    @Override
    public List<String> getInternNames() {
        return database.queryListCallable(Procedure.GET_ALL.getName(), new LinkedList<>(), resultSet -> resultSet.getString("InternName"));
    }

    @Override
    public UUID getCategoryId(String internName) {
        return database.queryCallable(Procedure.GET_ID_BY_NAME.getName(), null, resultSet -> {
            String id = resultSet.getString("ID");
            return id != null ? UUID.fromString(id) : null;
        }, internName);
    }

    @Override
    public String getInternName(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> resultSet.getString("InternName"), id.toString());
    }

    @Override
    public String getDisplayName(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> resultSet.getString("DisplayName"), id.toString());
    }

    @Override
    public Material getIcon(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> {
            String icon = resultSet.getString("Icon");
            return icon != null ? Material.getMaterial(icon) : null;
        }, id.toString());
    }

    @Override
    public String getDescription(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> resultSet.getString("Description"), id.toString());
    }

    @Override
    public String[] getLore(UUID id) {
        String description = getDescription(id);
        return description != null ? description.split("\n") : null;
    }

    @Override
    public ItemStack getItemStack(UUID id) {
        Material icon = getIcon(id);
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        // container.set(CityBuild.getKeyShopCategory(), PersistentDataType.STRING, id.toString());
        container.set(CityBuild.getKeyShopCategory(), PersistentDataType.BOOLEAN, true);
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(getDisplayName(id)));

        List<Component> lores = new LinkedList<>();
        String[] lore = getLore(id);
        if (lore != null)
            for (String line : lore)
                lores.add(MiniMessage.miniMessage().deserialize(line));
        itemMeta.lore(lores);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
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

                String internName = data[0];

                if (existsCategory(internName)) {
                    logger.warn("ShopCategory with ID {} already exists, skipping...", internName);
                    continue;
                }

                String displayName = data[1];
                String icon = data[2];
                String description = data[3];
                Material material = Material.getMaterial(icon);

                if (material == null) {
                    logger.error("Invalid material: {}; Data: {}", icon, data);
                    continue;
                }

                UUID id = UUID.randomUUID();
                addCategory(id, internName, displayName, material, description);
            }
        } catch (IOException e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
        logger.info("Importing categories from file: {} finished", file.getName());
    }

    private enum Procedure {
        ADD_CATEGORY("CB_ShopCategory_AddCategory", "cid UUID, iname TINYTEXT, display TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "INSERT INTO [TABLE] VALUES (cid,iname,display,mat,des);"),
        REMOVE_CATEGORY("CB_ShopCategory_RemoveCategory", "cid UUID", "DELETE FROM [TABLE] WHERE ID=cid;"),
        GET_DATA_WITH_NAME("CB_ShopCategory_GetDataWithName", "cid UUID, iname TINYTEXT", "SELECT * FROM [TABLE] WHERE ID=cid AND InternName=iname;"),
        GET_DATA("CB_ShopCategory_GetData", "cid UUID", "SELECT * FROM [TABLE] WHERE ID=cid;"),
        GET_ID_BY_NAME("CB_ShopCategory_GetIdByName", "iname TINYTEXT", "SELECT ID FROM [TABLE] WHERE InternName=iname;"),
        GET_ALL("CB_ShopCategory_GetAll", "", "SELECT ID, InternName FROM [TABLE];"),
        UPDATE_DISPLAY_NAME("CB_ShopCategory_UpdateCategory", "cid UUID, display TINYTEXT", "UPDATE [TABLE] SET DisplayName=display WHERE ID=cid;"),
        UPDATE_ICON("CB_ShopCategory_UpdateCategory", "cid UUID, mat TINYTEXT", "UPDATE [TABLE] SET Icon=mat WHERE ID=cid;"),
        UPDATE_DESCRIPTION("CB_ShopCategory_UpdateCategory", "cid UUID, des MEDIUMTEXT", "UPDATE [TABLE] SET Description=des WHERE ID=cid;");
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
