package de.murmelmeister.lobby.listener.listeners;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.lobby.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LobbyItemsListener extends Listeners {
    public LobbyItemsListener(Main main) {
        super(main);
    }

    @EventHandler
    public void interactItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        if (!(event.getAction().isRightClick())) return;
        if (itemStack.getItemMeta().getDisplayName().equals(HexColor.format(config.getString(Configs.LOBBY_ITEMS_NAVIGATOR_DISPLAY_NAME)))) {
            Inventory inventory = player.getServer().createInventory(null, 3 * 9, HexColor.format(config.getString(Configs.LOBBY_ITEMS_NAVIGATOR_INVENTORY_NAME)));
            inventory.setItem(0, createItemDefault());
            inventory.setItem(1, createItemDefault());
            inventory.setItem(2, createItemDefault());
            inventory.setItem(3, createItemDefault());
            inventory.setItem(4, createItemDefault());
            inventory.setItem(5, createItemDefault());
            inventory.setItem(6, createItemDefault());
            inventory.setItem(7, createItemDefault());
            inventory.setItem(8, createItemDefault());

            inventory.setItem(9, createItemDefault());
            inventory.setItem(10, createItemDefault());
            inventory.setItem(11, createItemCityBuild());
            inventory.setItem(12, createItemDefault());
            inventory.setItem(13, createItemDefault());
            inventory.setItem(14, createItemDefault());
            inventory.setItem(15, createItemComingSoon());
            inventory.setItem(16, createItemDefault());
            inventory.setItem(17, createItemDefault());

            inventory.setItem(18, createItemDefault());
            inventory.setItem(19, createItemDefault());
            inventory.setItem(20, createItemDefault());
            inventory.setItem(21, createItemDefault());
            inventory.setItem(22, createItemSpawn());
            inventory.setItem(23, createItemDefault());
            inventory.setItem(24, createItemDefault());
            inventory.setItem(25, createItemDefault());
            inventory.setItem(26, createItemDefault());
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        if (!(event.getView()).getTitle().equals(HexColor.format(config.getString(Configs.LOBBY_ITEMS_NAVIGATOR_INVENTORY_NAME))))
            return;
        event.setCancelled(true);

        if (currentItem.equals(createItemDefault())) event.setCancelled(true);
        if (currentItem.equals(createItemSpawn())) {
            player.closeInventory();
            if (locations.isSpawnExist()) {
                player.teleport(locations.getSpawn());
            } else {
                sendMessage(player, message.getString(Messages.EVENT_SPAWN_NOT_EXIST).replace("[PREFIX]", message.prefix()));
            }
        }
        if (currentItem.equals(createItemComingSoon())) event.setCancelled(true);
        if (currentItem.equals(createItemCityBuild())) {
            player.closeInventory();
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeUTF("Connect");
                out.writeUTF(config.getString(Configs.EVENT_LOBBY_ITEMS_CITY_BUILD));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            sendMessage(player, message.getString(Messages.EVENT_LOBBY_ITEMS_SEND_CITY_BUILD));
            player.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
        }
    }

    private ItemStack createItemDefault() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(HexColor.format("&0 "));
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createItemSpawn() {
        ItemStack itemStack = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(HexColor.format("&8» #3f00e4Spawn"));
        List<String> lores = new ArrayList<>();
        lores.add(HexColor.format("&7Click here to teleport to the spawn."));
        itemMeta.setLore(lores);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createItemComingSoon() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(HexColor.format("&cComing Soon"));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createItemCityBuild() {
        ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(HexColor.format("&8» #3f00e4CityBuild &8(&71.20.1&8)"));
        List<String> lores = new ArrayList<>();
        lores.add(HexColor.format("&7This is a CityBuild server that is"));
        lores.add(HexColor.format("&7special in the distant future."));
        lores.add(HexColor.format(""));
        lores.add(HexColor.format("&8-> &7Status&8: &aOnline"));
        lores.add(HexColor.format(""));
        lores.add(HexColor.format("&7Click to join the server."));
        itemMeta.setLore(lores);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
