package de.murmelmeister.murmelessentials.listener.listeners;

import de.murmelmeister.murmelapi.permission.PermissionConfig;
import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.listener.Listeners;
import de.murmelmeister.murmelessentials.util.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ColorListener extends Listeners {
    public ColorListener(Main main) {
        super(main);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(permission.getValueString(PermissionConfig.PERMISSION_COLOR_NORMAL)))
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        if (player.hasPermission(permission.getValueString(PermissionConfig.PERMISSION_COLOR_HEX)))
            event.setMessage(HexColor.format(event.getMessage()));
    }
}
