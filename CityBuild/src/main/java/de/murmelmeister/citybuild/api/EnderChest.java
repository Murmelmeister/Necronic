package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import de.murmelmeister.murmelapi.util.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class EnderChest extends PlayerInventoryLoader {
    private final Logger logger;
    private final Config defaultConfig;
    private final Message message;
    private Player target; // TODO: removed
    private OfflinePlayer offlineTarget; // TODO: removed
    private List<UUID> uuids;

    // TODO: Add a List<String> playerList
    // Bug: Wenn zwei Invs offen sind und einer geschlossen wird, überschreibt den inv auf dem anderem

    private File file;

    public EnderChest(Main main) {
        this.logger = main.getLogger();
        this.defaultConfig = main.getConfig();
        this.message = main.getMessage();
    }

    @Override
    public void create(UUID uuid) {
        String fileName = uuid + ".yml";
        this.file = new File(String.format("plugins//%s//EnderChest//UserData//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEnderChestGUI(Player player) {
        Inventory inventory = player.getServer().createInventory(null, 9, HexColor.format(message.getString(Messages.ENDER_CHEST_MENU_USE)));
        for (int i = 1; i < 10; i++)
            if (player.hasPermission(defaultConfig.getString(Configs.PERMISSION_ENDER_CHEST_SLOTS) + "." + i))
                inventory.setItem(i - 1, createItem(Material.matchMaterial(defaultConfig.getString(Configs.ENDER_CHEST_MATERIAL_UNLOCKED)), HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_UNLOCKED).replace("[SLOT]", "" + i))));
            else
                inventory.setItem(i - 1, createItem(Material.matchMaterial(defaultConfig.getString(Configs.ENDER_CHEST_MATERIAL_LOCKED)), HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_LOCKED))));
        player.openInventory(inventory);
    }

    public void createEnderChestGUI(Player player, Player target) {
        setTarget(target);
        Inventory inventory = player.getServer().createInventory(null, 9, HexColor.format(message.getString(Messages.ENDER_CHEST_MENU_OTHER).replace("[PLAYER]", target.getName())));
        for (int i = 1; i < 10; i++)
            if (target.hasPermission(defaultConfig.getString(Configs.PERMISSION_ENDER_CHEST_SLOTS) + "." + i))
                inventory.setItem(i - 1, createItem(Material.matchMaterial(defaultConfig.getString(Configs.ENDER_CHEST_MATERIAL_UNLOCKED)), HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_UNLOCKED).replace("[SLOT]", "" + i))));
            else
                inventory.setItem(i - 1, createItem(Material.matchMaterial(defaultConfig.getString(Configs.ENDER_CHEST_MATERIAL_LOCKED)), HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_LOCKED))));
        player.openInventory(inventory);
    }

    public void createEnderChestGUI(Player player, OfflinePlayer target) {
        setOfflineTarget(target);
        Inventory inventory = player.getServer().createInventory(null, 9, HexColor.format(message.getString(Messages.ENDER_CHEST_MENU_OTHER).replace("[PLAYER]", target.getName())));
        for (int i = 1; i < 10; i++)
            if (hasAccess(target.getUniqueId(), i))
                inventory.setItem(i - 1, createItem(Material.matchMaterial(defaultConfig.getString(Configs.ENDER_CHEST_MATERIAL_UNLOCKED)), HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_UNLOCKED).replace("[SLOT]", "" + i))));
            else
                inventory.setItem(i - 1, createItem(Material.matchMaterial(defaultConfig.getString(Configs.ENDER_CHEST_MATERIAL_LOCKED)), HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_LOCKED))));
        player.openInventory(inventory);
    }

    private ItemStack createItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public OfflinePlayer getOfflineTarget() {
        return offlineTarget;
    }

    public void setOfflineTarget(OfflinePlayer offlineTarget) {
        this.offlineTarget = offlineTarget;
    }
}
