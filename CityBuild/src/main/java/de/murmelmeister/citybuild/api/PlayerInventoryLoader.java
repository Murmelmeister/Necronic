package de.murmelmeister.citybuild.api;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class PlayerInventoryLoader {
    public YamlConfiguration config;
    public List<Integer> itemID;
    public List<String> enchantment;
    public List<String> lore;
    private Inventory inventory;

    public abstract void create(UUID uuid);

    public abstract void save();

    public Inventory loadInventory(Player player, int slot) {
        create(player.getUniqueId());
        String path = "Inventory." + slot;
        this.itemID = getItemID(slot);
        Inventory inventory = player.getServer().createInventory(null, getInt(path + ".Size"), getString(path + ".Name"));
        for (int i : itemID) {
            Material material = Material.matchMaterial(getString(path + "." + i + ".Name"));
            if (material != null) {
                String displayName = getString(path + "." + i + ".DisplayName");
                int amount = getInt(path + "." + i + ".Amount");
                this.enchantment = getEnchantment(slot, i);
                this.lore = getLore(slot, i);
                ItemStack itemStack = new ItemStack(material, amount);
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName(displayName);
                for (String name : enchantment) {
                    int enchantLevel = getInt(path + "." + i + ".Enchantments." + name + ".Level");
                    itemMeta.addEnchant(Enchantment.getByName(name), enchantLevel, true);
                }
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i, itemStack);
            }
        }
        return inventory;
    }

    public Inventory loadInventory(Player player, int slot, String title, int size) {
        create(player.getUniqueId());
        String path = "Inventory." + slot;
        String contents = getString(path + ".Contents");
        String storageContents = getString(path + ".StorageContents");
        Inventory inventory = player.getServer().createInventory(null, size, title);
        if (contents != null) inventory.setContents(loadItemStackArray(contents));
        if (storageContents != null) inventory.setStorageContents(loadItemStackArray(storageContents));
        return inventory;
    }

    public Inventory loadInventory(UUID uuid, Server server, int slot, String title, int size) {
        create(uuid);
        String path = "Inventory." + slot;
        String contents = getString(path + ".Contents");
        String storageContents = getString(path + ".StorageContents");
        Inventory inventory = server.createInventory(null, size, title);
        if (contents != null) inventory.setContents(loadItemStackArray(contents));
        if (storageContents != null) inventory.setStorageContents(loadItemStackArray(storageContents));
        return inventory;
    }

    public void saveInventory(UUID uuid, Inventory inventory, int slot) {
        create(uuid);
        String path = "Inventory." + slot;
        this.itemID = getItemID(slot);
        set(path + ".Name", inventory.getType().name());
        set(path + ".Size", inventory.getSize());
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                this.enchantment = getEnchantment(slot, i);
                this.lore = getLore(slot, i);
                set(path + "." + i + ".Name", itemStack.getType().name());
                set(path + "." + i + ".DisplayName", itemStack.getItemMeta().getDisplayName());
                set(path + "." + i + ".Amount", itemStack.getAmount());
                for (Map.Entry<Enchantment, Integer> entry : itemStack.getItemMeta().getEnchants().entrySet()) {
                    String enchantName = entry.getKey().getName();
                    if (!(enchantment.contains(enchantName))) enchantment.add(enchantName);
                    set(path + "." + i + ".Enchantments." + enchantName + ".Level", entry.getValue());
                }
                set(path + "." + i + ".Enchantments.ListName", enchantment);
                for (String s : Objects.requireNonNull(itemStack.getItemMeta().getLore()))
                    if (!(lore.contains(s))) lore.add(s);
                set(path + "." + i + ".Lore", lore);
                if (!(itemID.contains(i))) itemID.add(i);
            }
        }
        set(path + ".ListIDs", itemID.stream().sorted().collect(Collectors.toList()));
        save();
    }

    public void saveInventory(UUID uuid, Inventory inventory, int slot, String title) {
        create(uuid);
        String path = "Inventory." + slot;
        String contents = saveItemStackArray(inventory.getContents());
        String storageContents = saveItemStackArray(inventory.getStorageContents());
        set(path + ".Title", title);
        set(path + ".Size", inventory.getSize());
        set(path + ".Contents", contents);
        set(path + ".StorageContents", storageContents);
        save();
    }

    public void saveInventory(UUID uuid, Inventory inventory, int slot, String title, boolean access) {
        create(uuid);
        String path = "Inventory." + slot;
        String contents = saveItemStackArray(inventory.getContents());
        String storageContents = saveItemStackArray(inventory.getStorageContents());
        set(path + ".Title", title);
        set(path + ".Size", inventory.getSize());
        set(path + ".Access", access);
        set(path + ".Contents", contents);
        set(path + ".StorageContents", storageContents);
        save();
    }

    public List<Integer> getItemID(int slot) {
        this.itemID = new LinkedList<>();
        String path = "Inventory." + slot + ".ListIDs";
        if (config.contains(path)) itemID = config.getIntegerList(path);
        return itemID;
    }

    public List<String> getEnchantment(int slot, int itemID) {
        this.enchantment = new ArrayList<>();
        String path = "Inventory." + slot + "." + itemID + ".Enchantments.ListName";
        if (config.contains(path)) enchantment = config.getStringList(path);
        return enchantment;
    }

    public List<String> getLore(int slot, int itemID) {
        this.lore = new LinkedList<>();
        String path = "Inventory." + slot + "." + itemID + ".Lore";
        if (config.contains(path)) lore = config.getStringList(path);
        return lore;
    }

    public ItemStack[] loadItemStackArray(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode class type.", e);
        }
    }

    public String saveItemStackArray(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Unable to save item stacks.", e);
        }
    }

    public String getInventorySize(UUID uuid, int slot) {
        create(uuid);
        return getString("Inventory." + slot + ".Size");
    }

    public boolean hasAccess(UUID uuid, int slot) {
        create(uuid);
        return getBoolean("Inventory." + slot + ".Access");
    }

    public void setAccess(UUID uuid, int slot, boolean access) {
        create(uuid);
        set("Inventory." + slot + ".Access", access);
        save();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }
}
