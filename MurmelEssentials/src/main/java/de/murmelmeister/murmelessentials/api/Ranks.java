package de.murmelmeister.murmelessentials.api;

import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import de.murmelmeister.murmelessentials.configs.Config;
import de.murmelmeister.murmelessentials.util.HexColor;
import de.murmelmeister.murmelessentials.util.config.Configs;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.slf4j.Logger;

public class Ranks {
    private final Config config;

    public Ranks(Main main) {
        this.config = main.getConfig();
    }

    @Deprecated
    public void setRankChat(AsyncPlayerChatEvent event, Group group, Player player) {
        group.groups().forEach(s -> {
            if (player.hasPermission(config.getString(Configs.DEFAULT_PRE_PERMISSION) + s))
                /*
                When a new player joins he is not in "default", unless the group has the "default" permission.
                With * permission you have all tab/chat permission, there you have to give the external "-permission".
                */
                setFormat(event, group.chatPrefix(s), group.chatSuffix(s), group.chatColor(s));
        });
    }

    @Deprecated
    private void setFormat(AsyncPlayerChatEvent event, String prefix, String suffix, String color) {
        event.setFormat(HexColor.format(prefix + "%s" + suffix + config.getString(Configs.DEFAULT_CHAT_PREFIX_MESSAGE) + color) + "%s");
    }

    public void updatePlayerList(MurmelEssentials instance, Server server, Group group) {
        server.getScheduler().scheduleSyncRepeatingTask(instance, () -> server.getOnlinePlayers().forEach(player -> setPlayerList(group, player)), 20L, config.getLong(Configs.UPDATE_SCHEDULE_PLAYER_LIST) * 20L);
    }

    private void setPlayerList(Group group, Player player) {
        group.groups().forEach(s -> {
            if (player.hasPermission(config.getString(Configs.DEFAULT_PRE_PERMISSION) + s))
                player.setPlayerListName(HexColor.format(group.groupPrefix(s) + group.groupColor(s) + player.getName() + group.groupSuffix(s)));
        });
    }

    public void updatePlayerTeams(MurmelEssentials instance, Server server, Group group) {
        server.getScheduler().scheduleSyncRepeatingTask(instance, () -> server.getOnlinePlayers().forEach(player -> setPlayerTeams(group, player, instance.getSLF4JLogger())), 20L, config.getLong(Configs.UPDATE_SCHEDULE_TAB_LIST) * 20L);
    }

    private void setPlayerTeams(Group group, Player player, Logger logger) {
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
                if (target.hasPermission(config.getString(Configs.DEFAULT_PRE_PERMISSION) + s))
                            /*
                            When a new player joins he is not in "default", unless the group has the "default" permission.
                            With * permission you have all tab/chat permission, there you have to give the external "-permission".
                            */
                    team.addEntry(target.getName());
        });
    }
}
