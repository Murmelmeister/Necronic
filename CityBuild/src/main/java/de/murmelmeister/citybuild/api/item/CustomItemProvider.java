package de.murmelmeister.citybuild.api.item;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.murmelapi.database.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class CustomItemProvider implements CustomItem {
    private static final String TABLE_NAME = "CB_CustomItems";
    private final Database database;

    public CustomItemProvider(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "ID UUID PRIMARY KEY, InternName TINYTEXT UNIQUE, DisplayName TINYTEXT, Material TINYTEXT, Description MEDIUMTEXT");
    }

    @Override
    public boolean existsItem(UUID id) {
        return id != null && database.existsCallable(Procedure.GET_DATA.getName(), id.toString());
    }

    @Override
    public void addItem(String internName, String displayName, Material material) {
        addItem(internName, displayName, material, null);
    }

    @Override
    public void addItem(String internName, String displayName, Material material, String description) {
        addItem(UUID.randomUUID(), internName, displayName, material, description);
    }

    @Override
    public void addItem(UUID id, String internName, String displayName, Material material) {
        addItem(id, internName, displayName, material, null);
    }

    @Override
    public void addItem(UUID id, String internName, String displayName, Material material, String description) {
        database.updateCallable(Procedure.ADD_ITEM.getName(), id.toString(), internName, displayName, material.name(), description);
    }

    @Override
    public void removeItem(UUID id) {
        database.updateCallable(Procedure.REMOVE_ITEM.getName(), id.toString());
    }

    @Override
    public void updateItem(UUID id, String displayName, String iconName, Material material, String description) {
        database.updateCallable(Procedure.UPDATE_ITEM.getName(), id.toString(), displayName, iconName, material.name(), description);
    }

    @Override
    public List<UUID> getCustomItemIds() {
        return database.queryListCallable(Procedure.GET_ALL.getName(), new LinkedList<>(), resultSet -> UUID.fromString(resultSet.getString("ID")));
    }

    @Override
    public List<String> getInternNames() {
        return database.queryListCallable(Procedure.GET_ALL.getName(), new LinkedList<>(), resultSet -> resultSet.getString("InternName"));
    }

    @Override
    public UUID getCustomItemId(String internName) {
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
    public Component getDisplayName(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null,
                resultSet -> MiniMessage.miniMessage().deserialize(resultSet.getString("DisplayName")), id.toString());
    }

    @Override
    public Material getMaterial(UUID id) {
        return database.queryCallable(Procedure.GET_DATA.getName(), null, resultSet -> {
            String material = resultSet.getString("Material");
            return material != null ? Material.getMaterial(material) : null;
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
        Material material = getMaterial(id);
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        // container.set(CityBuild.getKeyCustomItems(), PersistentDataType.STRING, id.toString());
        container.set(CityBuild.getKeyCustomItems(), PersistentDataType.BOOLEAN, true);
        itemMeta.displayName(getDisplayName(id));

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
    public void addDefaultMaterials() {
        for (Material material : Material.values()) {
            if (material.isAir()) continue;
            if (!material.isItem()) continue;
            UUID id = UUID.randomUUID();
            if (existsItem(id)) continue;
            String displayName = new ItemStack(material).getI18NDisplayName();
            addItem(id, material.name().toLowerCase(), displayName, material);
        }
    }

    private enum Procedure {
        ADD_ITEM("CB_CustomItems_AddItem", "cid UUID, iname TINYTEXT, display TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "INSERT INTO [TABLE] VALUES (cid,iname,display,mat,des);"),
        REMOVE_ITEM("CB_CustomItems_RemoveItem", "cid UUID", "DELETE FROM [TABLE] WHERE ID=cid;"),
        GET_DATA("CB_CustomItems_GetData", "cid UUID", "SELECT * FROM [TABLE] WHERE ID=cid;"),
        GET_ID_BY_NAME("CB_CustomItems_GetIdByName", "iname TINYTEXT", "SELECT ID FROM [TABLE] WHERE InternName=iname;"),
        GET_ALL("CB_CustomItems_GetAll", "", "SELECT ID, InternName FROM [TABLE];"),
        UPDATE_ITEM("CB_CustomItems_UpdateItem", "cid UUID, display TINYTEXT, iname TINYTEXT, mat TINYTEXT, des MEDIUMTEXT",
                "UPDATE [TABLE] SET DisplayName=display, IconName=iname, Material=mat, Description=des WHERE ID=cid;");
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
