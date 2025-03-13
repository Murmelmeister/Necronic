package de.murmelmeister.citybuild.api;

import de.murmelmeister.murmelapi.database.Database;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static de.murmelmeister.citybuild.util.InventoryUtil.loadItems;
import static de.murmelmeister.citybuild.util.InventoryUtil.saveItems;

public final class PlayerInventory {
    private static final String TABLE_NAME = "CB_UserInventory";
    private final Database database;

    public PlayerInventory(Database database) {
        this.database = database;
        createTable();
        Procedure.loadAll(database);
    }

    private void createTable() {
        database.createTable(TABLE_NAME, "UserID INT PRIMARY KEY, Content MEDIUMTEXT, StorageContent MEDIUMTEXT, ArmorContent MEDIUMTEXT, ExtraContent MEDIUMTEXT, " +
                                        "EnderChestContent MEDIUMTEXT, EnderChestStorage MEDIUMTEXT");
    }

    public boolean existInventory(int userId) {
        return database.exists(Procedure.INVENTORY_GET.getName(), userId);
    }

    public void createOrUpdateInventory(int userId, Player player, boolean update) {
        String contents = saveItems(player.getInventory().getContents());
        String storageContents = saveItems(player.getInventory().getStorageContents());
        String armorContents = saveItems(player.getInventory().getArmorContents());
        String extraContents = saveItems(player.getInventory().getExtraContents());
        String enderChestContents = saveItems(player.getEnderChest().getContents());
        String enderChestStorage = saveItems(player.getEnderChest().getStorageContents());
        if (update)
            database.callUpdate(Procedure.INVENTORY_UPDATE.getName(), userId, contents, storageContents, armorContents, extraContents, enderChestContents, enderChestStorage);
        else
            database.callUpdate(Procedure.INVENTORY_CREATE.getName(), userId, contents, storageContents, armorContents, extraContents, enderChestContents, enderChestStorage);
    }

    public void deleteInventory(int userId) {
        database.callUpdate(Procedure.INVENTORY_DELETE.getName(), userId);
    }

    public void setInventory(int userId, Player player) {
        player.getInventory().setContents(getContents(userId));
        player.getInventory().setStorageContents(getStorageContents(userId));
        player.getInventory().setArmorContents(getArmorContents(userId));
        player.getInventory().setExtraContents(getExtraContents(userId));
        player.getEnderChest().setContents(getEnderChestContents(userId));
        player.getEnderChest().setStorageContents(getEnderChestStorage(userId));
    }

    public ItemStack[] getContents(int userId) {
        return loadItems(database.query(null, "Content", String.class, Procedure.INVENTORY_GET.getName(), userId));
    }

    public ItemStack[] getStorageContents(int userId) {
        return loadItems(database.query(null, "StorageContent", String.class, Procedure.INVENTORY_GET.getName(), userId));
    }

    public ItemStack[] getArmorContents(int userId) {
        return loadItems(database.query(null, "ArmorContent", String.class, Procedure.INVENTORY_GET.getName(), userId));
    }

    public ItemStack[] getExtraContents(int userId) {
        return loadItems(database.query(null, "ExtraContent", String.class, Procedure.INVENTORY_GET.getName(), userId));
    }

    public ItemStack[] getEnderChestContents(int userId) {
        return loadItems(database.query(null, "EnderChestContent", String.class, Procedure.INVENTORY_GET.getName(), userId));
    }

    public ItemStack[] getEnderChestStorage(int userId) {
        return loadItems(database.query(null, "EnderChestStorage", String.class, Procedure.INVENTORY_GET.getName(), userId));
    }

    private enum Procedure {
        INVENTORY_CREATE("PlayerInventory_Create", "uid INT, cont MEDIUMTEXT, storage MEDIUMTEXT, armor MEDIUMTEXT, extra MEDIUMTEXT, eccont MEDIUMTEXT, ecstorage MEDIUMTEXT",
                "INSERT INTO [TABLE] VALUES (uid, cont, storage, armor, extra, eccont, ecstorage);"),
        INVENTORY_UPDATE("PlayerInventory_Update", "uid INT, cont MEDIUMTEXT, storage MEDIUMTEXT, armor MEDIUMTEXT, extra MEDIUMTEXT, eccont MEDIUMTEXT, ecstorage MEDIUMTEXT",
                "UPDATE [TABLE] SET Content=cont, StorageContent=storage, ArmorContent=armor, ExtraContent=extra, EnderChestContent=eccont, EnderChestStorage=ecstorage WHERE UserID=uid;"),
        INVENTORY_DELETE("PlayerInventory_Delete", "uid INT", "DELETE FROM [TABLE] WHERE UserID=uid;"),
        INVENTORY_GET("PlayerInventory_Get", "uid INT", "SELECT * FROM [TABLE] WHERE UserID=uid;");
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
