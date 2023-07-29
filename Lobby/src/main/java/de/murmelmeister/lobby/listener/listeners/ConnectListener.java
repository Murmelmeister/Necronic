package de.murmelmeister.lobby.listener.listeners;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.listener.Listeners;
import de.murmelmeister.lobby.util.HexColor;
import de.murmelmeister.lobby.util.Items;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.lobby.util.config.Messages;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener extends Listeners {
    public ConnectListener(Main main) {
        super(main);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.getBoolean(Configs.EVENT_ENABLE_PLAYER_JOIN))
            if (player.canSee(player)) // Maybe unnecessary, but if someone is in vanish you should not see the message
                if (config.getBoolean(Configs.PREFIX_ENABLE))
                    event.setJoinMessage(HexColor.format(message.prefix() + message.getString(Messages.EVENT_PLAYER_JOIN).replace("[PLAYER]", player.getName())));
                else
                    event.setJoinMessage(HexColor.format(message.getString(Messages.EVENT_PLAYER_JOIN).replace("[PLAYER]", player.getName())));
            else event.setJoinMessage(null);
        else event.setJoinMessage(null);

        if (locations.isSpawnExist()) {
            if (config.getBoolean(Configs.EVENT_ENABLE_TELEPORT_TO_SPAWN))
                player.teleport(locations.getSpawn());
        } else
            sendMessage(player, message.getString(Messages.EVENT_SPAWN_NOT_EXIST).replace("[PREFIX]", message.prefix()));

        schedulerTask.setUsername(player);
        schedulerTask.clearBukkitTask(player);

        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_TAB_LIST)) setScoreboardTabList(player, instance.getServer());
        if (config.getBoolean(Configs.EVENT_ENABLE_JOIN_MESSAGE))
            player.sendMessage(HexColor.format(message.getString(Messages.EVENT_JOIN_MESSAGE).replace("[PREFIX]", message.prefix()).replace("[PLAYER]", player.getName())));
        if (config.getBoolean(Configs.EVENT_ENABLE_JOIN_TITLE))
            player.sendTitle(HexColor.format(message.getString(Messages.EVENT_JOIN_TITLE)).replace("[PREFIX]", message.prefix()).replace("[PLAYER]", player.getName()),
                    HexColor.format(message.getString(Messages.EVENT_JOIN_SUB_TITLE).replace("[PREFIX]", message.prefix()).replace("[PLAYER]", player.getName())));

        Items.setLobbyItems(config, player);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (config.getBoolean(Configs.EVENT_ENABLE_PLAYER_QUIT))
            if (player.canSee(player)) // Maybe unnecessary, but if someone is in vanish you should not see the message
                if (config.getBoolean(Configs.PREFIX_ENABLE))
                    event.setQuitMessage(HexColor.format(message.prefix() + message.getString(Messages.EVENT_PLAYER_QUIT).replace("[PLAYER]", player.getName())));
                else
                    event.setQuitMessage(HexColor.format(message.getString(Messages.EVENT_PLAYER_QUIT).replace("[PLAYER]", player.getName())));
            else event.setQuitMessage(null);
        else event.setQuitMessage(null);
        listUtil.getBuild().remove(player.getUniqueId());
        player.getServer().getScheduler().getPendingTasks().forEach(bukkitTask -> schedulerTask.removeBukkitTask(player, bukkitTask));
    }

    public void setScoreboardTabList(Player player, Server server) {
        schedulerTask.addBukkitTask(player,
                server.getScheduler().runTaskTimerAsynchronously(instance, () -> player.setPlayerListHeaderFooter(HexColor.format(message.getString(Messages.SCOREBOARD_TAB_LIST_HEADER)
                                .replace("[CURRENT_PLAYERS]", String.valueOf(server.getOnlinePlayers().size())).replace("[MAX_PLAYERS]", String.valueOf(server.getMaxPlayers())).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))),
                        HexColor.format(message.getString(Messages.SCOREBOARD_TAB_LIST_FOOTER))), 20L, config.getLong(Configs.SCOREBOARD_UPDATE_TAB_LIST) * 20L));
    }
}
