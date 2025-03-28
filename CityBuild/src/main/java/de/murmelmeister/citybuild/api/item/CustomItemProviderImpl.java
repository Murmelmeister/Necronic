package de.murmelmeister.citybuild.api.item;

import de.murmelmeister.murmelapi.database.Database;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CustomItemProviderImpl implements CustomItemProvider {
    private static final String TABLE_NAME = "CB_CustomItems";
    private final Database database;

    private final Map<UUID, CustomItem> cache = new ConcurrentHashMap<>();

    public CustomItemProviderImpl(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
        loadData();
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ID UUID PRIMARY KEY, DisplayName TINYTEXT, Material TINYTEXT, Description MEDIUMTEXT");
    }

    private void loadData() {
        cache.clear();
        database.queryProcess(resultSet -> {
            UUID id = UUID.fromString(resultSet.getString("ID"));
            String displayName = resultSet.getString("DisplayName");
            String material = resultSet.getString("Material");
            String description = resultSet.getString("Description");
            CustomItem customItem = new CustomItemImpl(id, displayName, material, description);
            cache.put(id, customItem);
        }, Procedure.GET_DATA.getName());
    }

    @Override
    public CustomItem getCustomItem(UUID id) {
        return cache.get(id);
    }

    @Override
    public boolean containsCustomItem(UUID id) {
        return cache.containsKey(id);
    }

    @Override
    public void addCustomItem(CustomItem customItem) {
        cache.put(customItem.getId(), customItem);
        database.asyncUpdate(Procedure.ADD_ITEM.getName(), customItem.getId(), customItem.getDisplayName(), customItem.getMaterial().name(), customItem.getDescription());
    }

    @Override
    public void removeCustomItem(UUID id) {
        cache.remove(id);
        database.asyncUpdate(Procedure.REMOVE_ITEM.getName(), id);
    }

    @Override
    public void updateCustomItem(CustomItem customItem) {
        cache.put(customItem.getId(), customItem);
        database.asyncUpdate(Procedure.UPDATE_ITEM.getName(), customItem.getId(), customItem.getDisplayName(), customItem.getMaterial().name(), customItem.getDescription());
    }

    @Override
    public List<UUID> getCustomItemIds() {
        return cache.keySet().stream().toList();
    }

    @Override
    public void addDefaultMaterials() {
        for (Material material : Material.values()) {
            if (material.isAir()) continue;
            if (!material.isItem()) continue;
            CustomItem customItem = new CustomItemImpl(UUID.randomUUID(), new ItemStack(material).getI18NDisplayName(), material.name());
            if (containsCustomItem(customItem.getId())) continue;
            addCustomItem(customItem);
        }
    }

    private enum Procedure {
        ADD_ITEM("CB_CustomItems_AddItem", "uid UUID, display TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "INSERT INTO [TABLE] VALUES (uid,display,mat,des);"),
        REMOVE_ITEM("CB_CustomItems_RemoveItem", "uid UUID", "DELETE FROM [TABLE] WHERE ID=uid;"),
        GET_DATA("CB_CustomItems_GetData", "", "SELECT * FROM [TABLE];"),
        UPDATE_ITEM("CB_CustomItems_UpdateItem", "uid UUID, display TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "UPDATE [TABLE] SET DisplayName=display, Material=mat, Description=des WHERE ID=uid;");
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
