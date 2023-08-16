package de.murmelmeister.murmelessentials.api;

import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.permission.PermissionConfig;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import de.murmelmeister.murmelessentials.util.HexColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.slf4j.Logger;

public class Ranks {

    @Deprecated
    public void setRankChat(AsyncPlayerChatEvent event, Permission permission, Player player) {
        Group group = permission.getGroup();
        group.groups().forEach(s -> {
            if (player.hasPermission(permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + s))
                /*
                When a new player joins he is not in "default", unless the group has the "default" permission.
                With * permission you have all tab/chat permission, there you have to give the external "-permission".
                */
                setFormat(event, permission, group.chatPrefix(s), group.chatSuffix(s), group.chatColor(s));
        });
    }

    @Deprecated
    private void setFormat(AsyncPlayerChatEvent event, Permission permission, String prefix, String suffix, String color) {
        event.setFormat(HexColor.format(prefix + "%s" + suffix + permission.getValueString(PermissionConfig.DEFAULT_CHAT_PREFIX_MESSAGE) + color) + "%s");
    }

    public void updatePlayerList(MurmelEssentials instance, Server server, Permission permission) {
        server.getScheduler().runTaskTimerAsynchronously(instance, () -> server.getOnlinePlayers().forEach(player -> setPlayerList(permission, player)), 20L, permission.getValueLong(PermissionConfig.UPDATE_SCHEDULE_PLAYER_LIST) * 20L);
    }

    private void setPlayerList(Permission permission, Player player) {
        Group group = permission.getGroup();
        group.groups().forEach(s -> {
            if (player.hasPermission(permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + s))
                player.setPlayerListName(HexColor.format(group.groupPrefix(s) + group.groupColor(s) + player.getName() + group.groupSuffix(s)));
        });
    }

    public void updatePlayerTeams(MurmelEssentials instance, Server server, Permission permission) {
        server.getScheduler().runTaskTimerAsynchronously(instance, () -> server.getOnlinePlayers().forEach(player -> setPlayerTeams(permission, player, instance.getSLF4JLogger())), 20L, permission.getValueLong(PermissionConfig.UPDATE_SCHEDULE_TAB_LIST) * 20L);
    }

    private void setPlayerTeams(Permission permission, Player player, Logger logger) {
        Group group = permission.getGroup();
        group.groups().forEach(s -> {
            Scoreboard scoreboard = player.getScoreboard();
            Team team = scoreboard.getTeam(group.teamID(s));
            if (team == null) team = scoreboard.registerNewTeam(group.teamID(s));
            team.setPrefix(HexColor.format(group.tabPrefix(s)));
            team.setSuffix(HexColor.format(group.tabSuffix(s)));
            try {
                team.setColor(ChatColor.getByChar(group.tabColor(s).replace("§", "").replace("&", "")));
            } catch (IllegalArgumentException e) {
                logger.warn("Please set the tab color in each group! Group: " + s);
            }
            for (Player target : player.getServer().getOnlinePlayers())
                if (target.hasPermission(permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + s))
                            /*
                            When a new player joins he is not in "default", unless the group has the "default" permission.
                            With * permission you have all tab/chat permission, there you have to give the external "-permission".
                            */
                    team.addEntry(target.getName());
        });
    }
}
