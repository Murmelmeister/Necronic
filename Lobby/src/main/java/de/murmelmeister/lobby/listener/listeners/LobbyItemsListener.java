package de.murmelmeister.lobby.listener.listeners;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
            // TODO: Add items to the inventory...
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        if (!(event.getView()).getTitle().equals(HexColor.format(config.getString(Configs.LOBBY_ITEMS_NAVIGATOR_INVENTORY_NAME)))) return;
        event.setCancelled(true);
        // TODO: Function of the items...
    }
}
