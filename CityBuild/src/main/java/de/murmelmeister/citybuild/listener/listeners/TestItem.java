package de.murmelmeister.citybuild.listener.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TestItem extends Listeners {
    public int testPoints = 0;

    public TestItem(Main main) {
        super(main);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getOpenInventory().setItem(20, createItem(Material.IRON_HELMET, "Test-1"));
        player.getOpenInventory().setItem(21, createItem(Material.IRON_CHESTPLATE, "Test-2"));
        player.getOpenInventory().setItem(22, createItem(Material.IRON_LEGGINGS, "Test-3"));
        player.getOpenInventory().setItem(23, createItem(Material.IRON_BOOTS, "Test-4"));

        schedulerTask.addBukkitTask(player, player.getServer().getScheduler().runTaskTimerAsynchronously(instance, () -> player.sendActionBar("TP: " + testPoints), 20L, 20L));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null) continue;
            if (itemStack.getItemMeta().getDisplayName().equals("Test-1")) testPoints = testPoints - 10;
            if (itemStack.getItemMeta().getDisplayName().equals("Test-2")) testPoints = testPoints - 20;
            if (itemStack.getItemMeta().getDisplayName().equals("Test-3")) testPoints = testPoints - 15;
            if (itemStack.getItemMeta().getDisplayName().equals("Test-4")) testPoints = testPoints - 5;
        }
    }

    @EventHandler
    public void armor(PlayerArmorChangeEvent event) {
        ItemStack newItem = event.getNewItem();
        ItemStack oldItem = event.getOldItem();
        if (newItem == null || oldItem == null) return;
        if (!newItem.hasItemMeta()) {
            if (oldItem.equals(createItem(Material.IRON_HELMET, "Test-1"))) testPoints = testPoints - 10;
            if (oldItem.equals(createItem(Material.IRON_CHESTPLATE, "Test-2"))) testPoints = testPoints - 20;
            if (oldItem.equals(createItem(Material.IRON_LEGGINGS, "Test-3"))) testPoints = testPoints - 15;
            if (oldItem.equals(createItem(Material.IRON_BOOTS, "Test-4"))) testPoints = testPoints - 5;
            return;
        }
        if (newItem.getItemMeta().getDisplayName().equals("Test-1")) testPoints = testPoints + 10;
        if (newItem.getItemMeta().getDisplayName().equals("Test-2")) testPoints = testPoints + 20;
        if (newItem.getItemMeta().getDisplayName().equals("Test-3")) testPoints = testPoints + 15;
        if (newItem.getItemMeta().getDisplayName().equals("Test-4")) testPoints = testPoints + 5;
    }

    public ItemStack createItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
