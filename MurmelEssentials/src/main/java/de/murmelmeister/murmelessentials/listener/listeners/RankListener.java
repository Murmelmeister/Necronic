package de.murmelmeister.murmelessentials.listener.listeners;

import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RankListener extends Listeners {
    public RankListener(Main main) {
        super(main);
    }

    @Deprecated
    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ranks.setRankChat(event, permission, player);
    }
}
