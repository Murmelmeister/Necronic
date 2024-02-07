package de.murmelmeister.citybuild.listener.listeners;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderChestListener extends Listeners {
    public EnderChestListener(Main main) {
        super(main);
    }

    @EventHandler
    public void handleEnderChestClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) return;
        if (!(currentItem.hasItemMeta())) return;
        if (currentItem.getItemMeta().getDisplayName().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_LOCKED)))) {
            event.setCancelled(true);
            player.closeInventory();
        }
        if (event.getView().getTitle().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MENU_USE)))) {
            event.setCancelled(true);
            for (int i = 1; i < 10; i++)
                if (currentItem.getItemMeta().getDisplayName().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_UNLOCKED).replace("[SLOT]", "" + i)))) {
                    int size = enderChest.getInventorySize(player.getUniqueId(), i) == null ? 6 * 9 : Integer.parseInt(enderChest.getInventorySize(player.getUniqueId(), i));
                    Inventory inventory = enderChest.loadInventory(player, i, HexColor.format("&dEnderChest - " + i), size); // TODO: Add config
                    enderChest.setInventory(inventory);
                    player.openInventory(enderChest.getInventory());
                }
        }

        Player target = enderChest.getTarget();
        if (target == null) {
            OfflinePlayer offlineTarget = enderChest.getOfflineTarget();
            if (offlineTarget == null) return;
            if (offlineTarget.isOnline() || offlineTarget.hasPlayedBefore()) {
                if (event.getView().getTitle().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MENU_OTHER).replace("[PLAYER]", offlineTarget.getName())))) {
                    event.setCancelled(true);
                    for (int i = 1; i < 10; i++)
                        if (currentItem.getItemMeta().getDisplayName().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_UNLOCKED).replace("[SLOT]", "" + i)))) {
                            int size = enderChest.getInventorySize(offlineTarget.getUniqueId(), i) == null ? 6 * 9 : Integer.parseInt(enderChest.getInventorySize(offlineTarget.getUniqueId(), i));
                            Inventory inventory = enderChest.loadInventory(offlineTarget.getUniqueId(), player.getServer(), i, HexColor.format("&dEnderChest - " + i + " - " + offlineTarget.getName()), size); // TODO: Add config
                            enderChest.setInventory(inventory);
                            player.openInventory(enderChest.getInventory());
                            event.setCancelled(true);
                        }
                }
            } else return;
            return;
        }

        if (event.getView().getTitle().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MENU_OTHER).replace("[PLAYER]", target.getName())))) {
            event.setCancelled(true);
            for (int i = 1; i < 10; i++)
                if (currentItem.getItemMeta().getDisplayName().equals(HexColor.format(message.getString(Messages.ENDER_CHEST_MATERIAL_UNLOCKED).replace("[SLOT]", "" + i)))) {
                    int size = enderChest.getInventorySize(target.getUniqueId(), i) == null ? 6 * 9 : Integer.parseInt(enderChest.getInventorySize(target.getUniqueId(), i));
                    Inventory inventory = enderChest.loadInventory(target, i, HexColor.format("&dEnderChest - " + i + " - " + target.getName()), size); // TODO: Add config
                    enderChest.setInventory(inventory);
                    player.openInventory(enderChest.getInventory());
                    event.setCancelled(true);
                }
        }
    }

    @EventHandler
    public void handleEnderChestClose(InventoryCloseEvent event) {
        for (int i = 1; i < 10; i++) {
            String title = HexColor.format("&dEnderChest - " + i);
            if (event.getView().getTitle().equals(title))
                enderChest.saveInventory(event.getPlayer().getUniqueId(), enderChest.getInventory(), i, title);

            Player target = enderChest.getTarget();
            if (target == null) {
                OfflinePlayer offlinePlayer = enderChest.getOfflineTarget();
                if (offlinePlayer == null) return;
                if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                    if (event.getView().getTitle().equals(title + " - " + offlinePlayer.getName()))
                        enderChest.saveInventory(offlinePlayer.getUniqueId(), enderChest.getInventory(), i, title);
                } else return;
                return;
            }
            if (event.getView().getTitle().equals(title + " - " + target.getName()))
                enderChest.saveInventory(target.getUniqueId(), enderChest.getInventory(), i, title);
        }
    }
}
