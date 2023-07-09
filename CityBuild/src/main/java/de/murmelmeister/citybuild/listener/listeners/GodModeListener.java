package de.murmelmeister.citybuild.listener.listeners;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class GodModeListener extends Listeners {
    public GodModeListener(Main main) {
        super(main);
    }

    @EventHandler
    public void handlePlayerDamage(EntityDamageEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
        if (player == null) return;
        if (listUtil.getGodMode().contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerFood(FoodLevelChangeEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
        if (player == null) return;
        if (listUtil.getGodMode().contains(player.getUniqueId())) event.setCancelled(true);
    }
}
