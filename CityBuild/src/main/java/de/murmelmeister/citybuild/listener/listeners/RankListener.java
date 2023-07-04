package de.murmelmeister.citybuild.listener.listeners;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
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
        for (String name : ranks.getRankList()) // The ranks are not sorted
            if (player.hasPermission(ranks.getPermission(name)))
                setFormat(event, ranks.getChatPrefix(name), ranks.getChatSuffix(name), ranks.getChatColor(name));
            else
                setFormat(event, ranks.defaultChatPrefix(), ranks.defaultChatSuffix(), ranks.defaultChatColor());
    }

    @Deprecated
    private void setFormat(AsyncPlayerChatEvent event, String prefix, String suffix, String color) {
        event.setFormat(HexColor.format(prefix + "%s" + suffix + config.getString(Configs.RANK_CHAT_PREFIX_MESSAGE) + color) + "%s");
    }
}
