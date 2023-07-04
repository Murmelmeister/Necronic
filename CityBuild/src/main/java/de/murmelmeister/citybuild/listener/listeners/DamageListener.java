package de.murmelmeister.citybuild.listener.listeners;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener extends Listeners {
    public DamageListener(Main main) {
        super(main);
    }

    @EventHandler
    public void handlePlayerGodMode(EntityDamageEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
        if (player == null) return;
        if (listUtil.getGodMode().contains(player.getUniqueId())) event.setCancelled(true);
    }
}
