package de.murmelmeister.murmelessentials.listener.listeners;

import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.api.CustomPermission;
import de.murmelmeister.murmelessentials.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.Field;

public class PermissionListener extends Listeners {
    public PermissionListener(Main main) {
        super(main);
    }

    @EventHandler
    public void playerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        try {
            Field field = Class.forName("org.bukkit.craftbukkit." +
                    player.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".entity.CraftHumanEntity").getDeclaredField("perm");
            field.setAccessible(true);
            field.set(player, new CustomPermission(player, permission));
            field.setAccessible(false);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
