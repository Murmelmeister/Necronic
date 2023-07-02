package de.murmelmeister.citybuild.listener.listeners;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
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
        if (player.hasPermission(config.getString(Configs.PERMISSION_COLOR_NORMAL)))
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        if (player.hasPermission(config.getString(Configs.PERMISSION_COLOR_HEX)))
            event.setMessage(HexColor.format(event.getMessage()));
    }
}
