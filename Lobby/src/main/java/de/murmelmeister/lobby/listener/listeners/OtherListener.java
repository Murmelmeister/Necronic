package de.murmelmeister.lobby.listener.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class OtherListener extends Listeners {
    public OtherListener(Main main) {
        super(main);
    }

    @EventHandler
    public void handleFoodLevelChange(FoodLevelChangeEvent event) {
        if (config.getBoolean(Configs.EVENT_PROTECTED_FOOD_LEVEL_CHANGE)) event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerDropItem(PlayerDropItemEvent event) {
        if (config.getBoolean(Configs.EVENT_PROTECTED_PLAYER_DROP_ITEM)) event.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void handlePlayerPickupItem(PlayerPickupItemEvent event) {
        if (config.getBoolean(Configs.EVENT_PROTECTED_PLAYER_PICKUP_ITEM)) event.setCancelled(true);
    }

    @EventHandler
    public void handleEntityExplode(EntityExplodeEvent event) {
        if (config.getBoolean(Configs.EVENT_PROTECTED_ENTITY_EXPLODE))
            event.setCancelled(!(event.getEntity() instanceof Player));
    }

    @EventHandler
    public void handleBlockExplode(BlockExplodeEvent event) {
        if (config.getBoolean(Configs.EVENT_PROTECTED_BLOCK_EXPLODE)) {
            event.setCancelled(true);
            event.setYield(0f);
        }
    }

    @EventHandler
    public void handlePlayerLevelChange(PlayerLevelChangeEvent event) {
        event.getPlayer().setLevel(config.getInt(Configs.EVENT_PROTECTED_PLAYER_CHANGE_LEVEL));
        event.getPlayer().setExp((float) config.getDouble(Configs.EVENT_PROTECTED_PLAYER_CHANGE_EXP));
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        event.setExpToDrop(0);
        event.setCancelled(!listUtil.getBuild().contains(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(!listUtil.getBuild().contains(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void handlePlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (listUtil.getBuild().contains(player.getUniqueId())) {
            event.setCancelled(false);
            return;
        }

        if (player.getLocation().getBlockY() <= locations.getSpawnHeight())
            if (locations.isSpawnExist()) player.teleport(locations.getSpawn());
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler
    public void handlePlayerInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        // Build mode can use the build mode inventory or settings inventory (It is a bug)
        if (listUtil.getBuild().contains(player.getUniqueId()))
            event.setCancelled(!event.getView().getPlayer().equals(player));
        else event.setCancelled(event.getView().getPlayer().equals(player));
    }

    @EventHandler
    public void handleFarmLandEvent(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        Block block = event.getPlayer().getLocation().getBlock();

        if (block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.WHEAT)) {
            if (player.isJumping()) event.setCancelled(true);
            else event.setCancelled(true);
        }
    }
}
