package de.murmelmeister.citybuild.api.item;

import de.murmelmeister.murmelapi.database.Database;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class CustomItems {
    private static final String TABLE_NAME = "CB_CustomItems";
    private final Database database;

    public CustomItems(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ItemID VARCHAR(255) PRIMARY KEY, DisplayName TINYTEXT, Material TINYTEXT, Lore MEDIUMTEXT, IsStatic BOOLEAN");
    }

    public boolean existItem(String itemId) {
        return database.exists(Procedure.ITEM_GET.getName(), itemId);
    }

    public void addItem(String itemId, String displayName, String material, String lore, boolean isStatic) {
        if (existItem(itemId)) return;
        byte staticItem = (byte) (isStatic ? 1 : 0);
        database.callUpdate(Procedure.ITEM_ADD.getName(), itemId, displayName, material, lore, staticItem);
    }

    public void addItem(String itemId, String displayName, Material material, String lore, boolean isStatic) {
        addItem(itemId, displayName, material.name(), lore, isStatic);
    }

    public void removeItem(String itemId) {
        database.callUpdate(Procedure.ITEM_REMOVE.getName(), itemId);
    }

    public String getDisplayName(String itemId) {
        return database.query(null, "DisplayName", String.class, Procedure.ITEM_GET.getName(), itemId);
    }

    public String getMaterialName(String itemId) {
        return database.query(null, "Material", String.class, Procedure.ITEM_GET.getName(), itemId);
    }

    public Material getMaterial(String itemId) {
        String material = getMaterialName(itemId);
        return material == null ? null : Material.getMaterial(material);
    }

    public String getLore(String itemId) {
        return database.query(null, "Lore", String.class, Procedure.ITEM_GET.getName(), itemId);
    }

    public boolean isStatic(String itemId) {
        return database.query(false, "IsStatic", boolean.class, Procedure.ITEM_GET.getName(), itemId);
    }

    public void updateItem(String itemId, String displayName, String material, String lore, boolean isStatic) {
        byte staticItem = (byte) (isStatic ? 1 : 0);
        database.callUpdate(Procedure.ITEM_UPDATE.getName(), itemId, displayName, material, lore, staticItem);
    }

    public void updateItem(String itemId, String displayName, Material material, String lore, boolean isStatic) {
        updateItem(itemId, displayName, material.name(), lore, isStatic);
    }

    public List<String> getItemIDs() {
        return database.queryList(new ArrayList<>(), "ItemID", String.class, Procedure.ITEM_GET_ALL.getName());
    }

    public void loadAllMaterials() {
        for (Material material : Material.values()) {
            if (!material.isItem()) continue;
            ItemStack itemStack = new ItemStack(material);
            addItem(material.name(), itemStack.getI18NDisplayName(), material, null, true);
        }
    }

    private enum Procedure {
        ITEM_ADD("CustomItem_Add", "iid VARCHAR(255), display TINYTEXT, imaterial TINYTEXT, ilore MEDIUMTEXT, static BOOLEAN",
                "INSERT INTO [TABLE] VALUES (iid, display, imaterial, ilore, static);"),
        ITEM_REMOVE("CustomItem_Remove", "iid VARCHAR(255)", "DELETE FROM [TABLE] WHERE ItemID=iid;"),
        ITEM_GET("CustomItem_Get", "iid VARCHAR(255)", "SELECT * FROM [TABLE] WHERE ItemID=iid;"),
        ITEM_GET_ALL("CustomItem_GetAll", "", "SELECT * FROM [TABLE];"),
        ITEM_UPDATE("CustomItem_Update", "iid VARCHAR(255), display TINYTEXT, imaterial TINYTEXT, ilore MEDIUMTEXT, static BOOLEAN",
                "UPDATE [TABLE] SET DisplayName=display, Material=imaterial, Lore=ilore, IsStatic=static WHERE ItemID=iid;");
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
