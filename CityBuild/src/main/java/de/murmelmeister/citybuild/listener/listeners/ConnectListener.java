package de.murmelmeister.citybuild.listener.listeners;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.listener.Listeners;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import de.murmelmeister.citybuild.util.scoreboard.TestScoreboard;
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

        if (locations.hasLocation("Spawn")) {
            if (config.getBoolean(Configs.EVENT_ENABLE_TELEPORT_TO_SPAWN))
                player.teleport(locations.getLocation("Spawn"));
        } else
            sendMessage(player, message.getString(Messages.EVENT_SPAWN_NOT_EXIST).replace("[PREFIX]", message.prefix()));

        homes.createUsername(player);
        schedulerTask.setUsername(player);
        cooldown.setUsername(player);
        economy.setUsername(player.getUniqueId(), player.getName());
        economy.createAccount(player.getUniqueId());
        if (player.hasPermission(config.getString(Configs.PERMISSION_JOIN_FLY))) {
            player.setAllowFlight(true);
            player.setFlying(true);
            sendMessage(player, message.getString(Messages.EVENT_JOIN_AUTO_FLY));
        }

        if (player.hasPermission(config.getString(Configs.PERMISSION_JOIN_GOD_MODE))) {
            listUtil.getGodMode().add(player.getUniqueId());
            sendMessage(player, message.getString(Messages.EVENT_JOIN_AUTO_GOD_MODE));
        }

        if (config.getBoolean(Configs.RANK_ENABLE_TAB))
            ranks.setTabRank(player); // If you don't have the permission for the ranks then you don't have a scoreboard and team
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_TAB_LIST)) ranks.setScoreboardTabList(player);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCOREBOARD)) new TestScoreboard(player, main);
        if (config.getBoolean(Configs.EVENT_ENABLE_JOIN_MESSAGE))
            player.sendMessage(HexColor.format(message.getString(Messages.EVENT_JOIN_MESSAGE).replace("[PREFIX]", message.prefix()).replace("[PLAYER]", player.getName())));
        if (config.getBoolean(Configs.EVENT_ENABLE_JOIN_TITLE))
            player.sendTitle(HexColor.format(message.getString(Messages.EVENT_JOIN_TITLE)).replace("[PREFIX]", message.prefix()).replace("[PLAYER]", player.getName()),
                    HexColor.format(message.getString(Messages.EVENT_JOIN_SUB_TITLE).replace("[PREFIX]", message.prefix()).replace("[PLAYER]", player.getName())));
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
        listUtil.getGodMode().remove(player.getUniqueId());
        listUtil.getLive().remove(player.getUniqueId());
        player.getServer().getScheduler().getPendingTasks().forEach(bukkitTask -> schedulerTask.removeBukkitTask(player, bukkitTask));
    }
}
