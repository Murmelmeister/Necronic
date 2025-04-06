package de.murmelmeister.citybuild.api.enderchest;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.database.Database;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.murmelmeister.citybuild.util.InventoryUtil.loadItems;
import static de.murmelmeister.citybuild.util.InventoryUtil.saveItems;

public final class EnderChestEditor {
    private static final String TABLE_NAME = "CB_EnderChests";
    private final Database database;
    private final ConfigFile configFile;

    public EnderChestEditor(Database database, ConfigFile configFile) {
        this.database = database;
        this.configFile = configFile;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "UserID INT, Slot INT, PRIMARY KEY (UserID, Slot), Content MEDIUMTEXT, StorageContent MEDIUMTEXT");
    }

    public boolean exists(int userId, int slot) {
        return database.exists(Procedure.ENDER_CHEST_GET.getName(), userId, slot);
    }

    public void createOrUpdate(int userId, int slot, Inventory inventory, boolean update) {
        ItemStack[] contents = inventory.getContents();
        if (contents.length > 0) contents = Arrays.copyOf(contents, contents.length - 9);

        String content = saveItems(contents);
        String storageContent = saveItems(inventory.getStorageContents());
        if (update)
            database.updateCallable(Procedure.ENDER_CHEST_UPDATE.getName(), userId, slot, content, storageContent);
        else
            database.updateCallable(Procedure.ENDER_CHEST_CREATE.getName(), userId, slot, content, storageContent);
    }

    public void delete(int userId, int slot) {
        database.updateCallable(Procedure.ENDER_CHEST_DELETE.getName(), userId, slot);
    }

    public void deleteAll(int userId) {
        database.updateCallable(Procedure.ENDER_CHEST_DELETE_ALL.getName(), userId);
    }

    public ItemStack[] getContents(int userId, int slot) {
        return loadItems(database.queryCallable(Procedure.ENDER_CHEST_GET.getName(), null, resultSet -> resultSet.getString("Content"), userId, slot));
    }

    public ItemStack[] getStorageContents(int userId, int slot) {
        return loadItems(database.query(Procedure.ENDER_CHEST_GET.getName(), null, resultSet -> resultSet.getString("StorageContent"), userId, slot));
    }

    public void setInventory(int userId, int slot, Inventory inventory) {
        ItemStack[] contents = getContents(userId, slot);
        ItemStack[] storageContents = getStorageContents(userId, slot);

        inventory.setContents(contents);
        inventory.setStorageContents(storageContents);
    }

    public List<Integer> getSlots(int userId) {
        return database.queryListCallable(Procedure.ENDER_CHEST_GET_ALL.getName(), new ArrayList<>(), resultSet -> resultSet.getInt("Slot"), userId);
    }

    public ItemStack getLockedIcon(int slot) {
        Material material = Material.getMaterial(configFile.getString(Configs.ENDER_CHEST_MATERIAL_LOCKED));
        if (material == null) return null;
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(CityBuild.getKeyEnderChest(), PersistentDataType.BOOLEAN, true);
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(configFile.getString(Configs.ENDER_CHEST_DISPLAY_NAME_LOCKED).replace("[SLOT]", slot + "")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getUnlockedIcon(int slot) {
        Material material = Material.getMaterial(configFile.getString(Configs.ENDER_CHEST_MATERIAL_UNLOCKED));
        if (material == null) return null;
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(CityBuild.getKeyEnderChest(), PersistentDataType.BOOLEAN, true);
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(configFile.getString(Configs.ENDER_CHEST_DISPLAY_NAME_UNLOCKED).replace("[SLOT]", slot + "")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private enum Procedure {
        ENDER_CHEST_CREATE("EnderChest_Create", "uid INT, sid INT, cont MEDIUMTEXT, storage MEDIUMTEXT", "INSERT INTO [TABLE] VALUES (uid, sid, cont, storage);"),
        ENDER_CHEST_UPDATE("EnderChest_Update", "uid INT, sid INT, cont MEDIUMTEXT, storage MEDIUMTEXT", "UPDATE [TABLE] SET Content=cont, StorageContent=storage WHERE UserID=uid AND Slot=sid;"),
        ENDER_CHEST_DELETE("EnderChest_Delete", "uid INT, sid INT", "DELETE FROM [TABLE] WHERE UserID=uid AND Slot=sid;"),
        ENDER_CHEST_DELETE_ALL("EnderChest_DeleteAll", "uid INT", "DELETE FROM [TABLE] WHERE UserID=uid;"),
        ENDER_CHEST_GET("EnderChest_Get", "uid INT, sid INT", "SELECT * FROM [TABLE] WHERE UserID=uid AND Slot=sid;"),
        ENDER_CHEST_GET_ALL("EnderChest_GetAll", "uid INT", "SELECT * FROM [TABLE] WHERE UserID=uid;");
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
