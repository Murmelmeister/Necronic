package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.util.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemValue {
    private final Logger logger;
    private final Config defaultConfig;
    private final Economy economy;

    private File file;
    private YamlConfiguration config;
    private List<String> items;

    public ItemValue(Main main) {
        this.logger = main.getLogger();
        this.defaultConfig = main.getConfig();
        this.economy = main.getEconomy();
    }

    public void register() {
        create();
        load();
        save();
    }

    public void create() {
        String fileName = "itemValue.yml";
        this.file = new File(String.format("plugins//%s//Economy//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        for (Material material : Material.values()) {
            if (get(material.name() + ".Value") == null) setValue(material, defaultSellPrice());
            if (get(material.name() + ".ID") == null) set(material.name() + ".ID", UUID.randomUUID().toString());
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal sellItem(Player player, ItemStack itemStack) {
        int amount = itemStack.getAmount();
        BigDecimal price = BigDecimal.valueOf(getValue(itemStack.getType()));
        if (amount <= 0)
            return BigDecimal.ZERO;
        BigDecimal result = price.multiply(BigDecimal.valueOf(amount));
        ItemStack item = itemStack.clone();
        item.setAmount(amount);
        if (!(player.getInventory().containsAtLeast(item, amount)))
            player.sendMessage(" Trying to remove more items than are available.");
        player.getInventory().removeItem(item);
        player.updateInventory();
        economy.addMoney(player.getUniqueId(), result);
        return result;
    }

    public BigDecimal defaultSellPrice() {
        return BigDecimal.valueOf(defaultConfig.getDouble(Configs.ECONOMY_DEFAULT_ITEM_SELL_PRICE));
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    private Object get(String path) {
        return config.get(path);
    }

    public double getValue(Material material) {
        return config.getDouble(material.name() + ".Value");
    }

    public String getID(Material material) {
        return config.getString(material.name() + ".ID");
    }

    public void setValue(Material material, BigDecimal price) {
        this.items = getItems();
        if (!(items.contains(material.name()))) {
            items.add(material.name());
            set("ItemList", items);
        }
        set(material.name() + ".Value", price);
    }

    public ItemStack createItem(Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setItemMeta(createItemMeta(itemStack));
        return itemStack;
    }

    public ItemMeta createItemMeta(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(HexColor.format("#222222ID: " + getID(itemStack.getType())));
        itemMeta.setLore(lore);
        return itemMeta;
    }

    public boolean existName(String name) {
        return config.getString(name) != null;
    }

    public List<String> getItems() {
        this.items = new ArrayList<>();
        if (config.contains("ItemList")) items = config.getStringList("ItemList");
        return items;
    }
}
