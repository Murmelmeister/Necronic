package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.ConfigUtil;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Ranks {
    private final Logger logger;
    private final Server server;
    private final CityBuild instance;
    private final Config defaultConfig;
    private final Message message;
    private final SchedulerTask schedulerTask;

    private File file;
    private YamlConfiguration config;
    private List<String> rankList;

    public Ranks(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.server = main.getInstance().getServer();
        this.instance = main.getInstance();
        this.defaultConfig = main.getConfig();
        this.message = main.getMessage();
        this.schedulerTask = main.getSchedulerTask();
    }

    public void register() {
        create();
        load();
        save();
    }

    public void create() {
        String fileName = "ranks.yml";
        this.file = new File(String.format("plugins//%s//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        if (!(existRank(defaultConfig.getString(Configs.RANK_DEFAULT_NAME)))) defaultRank();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void defaultRank() {
        addRank(defaultConfig.getString(Configs.RANK_DEFAULT_NAME),
                defaultConfig.getString(Configs.RANK_DEFAULT_CHAT_PREFIX),
                defaultConfig.getString(Configs.RANK_DEFAULT_CHAT_SUFFIX),
                defaultConfig.getString(Configs.RANK_DEFAULT_CHAT_COLOR),
                defaultConfig.getString(Configs.RANK_DEFAULT_TAB_PREFIX),
                defaultConfig.getString(Configs.RANK_DEFAULT_TAB_SUFFIX),
                defaultConfig.getString(Configs.RANK_DEFAULT_TAB_COLOR),
                defaultConfig.getString(Configs.RANK_DEFAULT_TAB_ID));
    }

    public void setTabRank(Player player) {
        schedulerTask.addBukkitTask(player,
                server.getScheduler().runTaskTimerAsynchronously(instance, () -> getRankList().forEach(s -> {
                    Scoreboard scoreboard = player.getScoreboard();

                    Team team = scoreboard.getTeam(getTeamID(s));
                    if (team == null) team = scoreboard.registerNewTeam(getTeamID(s));
                    team.setPrefix(HexColor.format(getTabPrefix(s)));
                    team.setSuffix(HexColor.format(getTabSuffix(s)));
                    //team.setColor(ChatColor.valueOf(ChatColor.getLastColors(getTabColor(s))));
                    team.setColor(Objects.requireNonNull(ChatColor.getByChar(getTabColor(s).replace("§", ""))));

                    for (Player target : server.getOnlinePlayers()) {
                        /*if (team.hasEntry(target.getName()))
                            if (listUtil.getLive().contains(player.getUniqueId()))
                                team.setSuffix(HexColor.format(defaultConfig.getString(Configs.RANK_LIVE_SUFFIX))); // Does not work great
                            else team.setSuffix(HexColor.format(getTabSuffix(s)));*/

                        if (target.hasPermission(getPermission(s)))
                            /*
                            When a new player joins he is not in "default", unless the group has the "default" permission.
                            With * permission you have all tab/chat permission, there you have to give the external "-permission".
                            */
                            team.addEntry(target.getName());
                    }
                }), 20L, defaultConfig.getLong(Configs.RANK_UPDATE_TAB_TIMER) * 20L));
    }

    public void setScoreboardTabList(Player player) {
        schedulerTask.addBukkitTask(player,
                server.getScheduler().runTaskTimerAsynchronously(instance, () -> player.setPlayerListHeaderFooter(HexColor.format(message.getString(Messages.SCOREBOARD_TAB_LIST_HEADER)
                                .replace("[CURRENT_PLAYERS]", String.valueOf(server.getOnlinePlayers().size())).replace("[MAX_PLAYERS]", String.valueOf(server.getMaxPlayers())).replace("[SERVER]", defaultConfig.getString(Configs.CURRENT_SERVER))),
                        HexColor.format(message.getString(Messages.SCOREBOARD_TAB_LIST_FOOTER))), 20L, defaultConfig.getLong(Configs.SCOREBOARD_UPDATE_TAB_LIST) * 20L));
    }

    public void addRank(String name, String chatPrefix, String chatSuffix, String chatColor, String tabPrefix, String tabSuffix, String tabColor, String tabId) {
        String path = "Ranks." + name;
        create();
        this.rankList = getRankList();
        if (!(rankList.contains(name))) {
            rankList.add(name);
            set("RankList", rankList);
        }

        set(path + ".Name", name);
        set(path + ".Chat.Prefix", chatPrefix);
        set(path + ".Chat.Suffix", chatSuffix);
        set(path + ".Chat.Color", chatColor);
        set(path + ".Tab.Prefix", tabPrefix);
        set(path + ".Tab.Suffix", tabSuffix);
        set(path + ".Tab.Color", tabColor);
        set(path + ".Tab.ID", tabId);
        set(path + ".TeamID", tabId + name);
        set(path + ".Scoreboard", name);
        set(path + ".Permission", defaultConfig.getString(Configs.RANK_PRE_PERMISSION) + name);
        save();
    }

    public void removeRank(String name) {
        create();
        this.rankList = getRankList();
        rankList.remove(name);
        set("RankList", rankList);
        set("Ranks." + name, null);
        save();
    }

    public String getRank(String name) {
        create();
        return HexColor.format(message.getString(Messages.COMMAND_RANK_GET)
                .replace("[PREFIX]", message.prefix())
                .replace("[NAME]", name)
                .replace("[CHAT_PREFIX]", getChatPrefix(name))
                .replace("[CHAT_SUFFIX]", getChatSuffix(name))
                .replace("[CHAT_COLOR]", getChatColor(name))
                .replace("[TAB_PREFIX]", getTabPrefix(name))
                .replace("[TAB_SUFFIX]", getTabSuffix(name))
                .replace("[TAB_COLOR]", getTabColor(name))
                .replace("[TAB_ID]", getTabID(name))
                .replace("[TEAM_ID]", getTeamID(name))
                .replace("[SCOREBOARD]", getScoreboard(name))
                .replace("[PERMISSION]", getPermission(name))
        );
    }

    public boolean existRank(String name) {
        create();
        return config.get("Ranks." + name) != null;
    }

    public String getChatPrefix(String name) {
        create();
        return getString("Ranks." + name + ".Chat.Prefix");
    }

    public void setChatPrefix(String name, String chatPrefix) {
        create();
        set("Ranks." + name + ".Chat.Prefix", chatPrefix);
        save();
    }

    public String getChatSuffix(String name) {
        create();
        return getString("Ranks." + name + ".Chat.Suffix");
    }

    public void setChatSuffix(String name, String chatSuffix) {
        create();
        set("Ranks." + name + ".Chat.Suffix", chatSuffix);
        save();
    }

    public String getChatColor(String name) {
        create();
        return getString("Ranks." + name + ".Chat.Color");
    }

    public void setChatColor(String name, String chatColor) {
        create();
        set("Ranks." + name + ".Chat.Color", chatColor);
        save();
    }

    public String getTabPrefix(String name) {
        create();
        return getString("Ranks." + name + ".Tab.Prefix");
    }

    public void setTabPrefix(String name, String tabPrefix) {
        create();
        set("Ranks." + name + ".Tab.Prefix", tabPrefix);
        save();
    }

    public String getTabSuffix(String name) {
        create();
        return getString("Ranks." + name + ".Tab.Suffix");
    }

    public void setTabSuffix(String name, String tabSuffix) {
        create();
        set("Ranks." + name + ".Tab.Suffix", tabSuffix);
        save();
    }

    public String getTabColor(String name) {
        create();
        return getString("Ranks." + name + ".Tab.Color");
    }

    public void setTabColor(String name, String tabColor) {
        create();
        set("Ranks." + name + ".Tab.Color", tabColor);
        save();
    }

    public String getTabID(String name) {
        create();
        return getString("Ranks." + name + ".Tab.ID");
    }

    public void setTabID(String name, String tabID) {
        create();
        set("Ranks." + name + ".Tab.ID", tabID);
        save();
    }

    public String getTeamID(String name) {
        create();
        return getString("Ranks." + name + ".TeamID");
    }

    public void setTeamID(String name, String teamID) {
        create();
        set("Ranks." + name + ".TeamID", teamID);
        save();
    }

    public String getScoreboard(String name) {
        create();
        return getString("Ranks." + name + ".Scoreboard");
    }

    public void setScoreboard(String name, String scoreboard) {
        create();
        set("Ranks." + name + ".Scoreboard", scoreboard);
        save();
    }

    public String getPermission(String name) {
        create();
        return getString("Ranks." + name + ".Permission");
    }

    public void setPermission(String name, String permission) {
        create();
        set("Ranks." + name + ".Permission", permission);
        save();
    }

    public List<String> getRankList() {
        this.rankList = new ArrayList<>();
        if (config.contains("RankList")) rankList = config.getStringList("RankList");
        return rankList;
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }

    private String getString(String path) {
        return config.getString(path);
    }
}
