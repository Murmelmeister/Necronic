package de.murmelmeister.lobby.listener.listeners;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RankListener extends Listeners {
    public RankListener(Main main) {
        super(main);
    }

    @Deprecated
    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        for (String name : ranks.getRankList())
            if (player.hasPermission(ranks.getPermission(name)))
                /*
                When a new player joins he is not in "default", unless the group has the "default" permission.
                With * permission you have all tab/chat permission, there you have to give the external "-permission".
                */
                setFormat(event, ranks.getChatPrefix(name), ranks.getChatSuffix(name), ranks.getChatColor(name));
    }

    @Deprecated
    private void setFormat(AsyncPlayerChatEvent event, String prefix, String suffix, String color) {
        event.setFormat(HexColor.format(prefix + "%s" + suffix + config.getString(Configs.RANK_CHAT_PREFIX_MESSAGE) + color) + "%s");
    }
}
