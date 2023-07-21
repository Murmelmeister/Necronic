package de.murmelmeister.lobby.util.config;

import org.bukkit.ChatColor;

public enum Configs {

    PREFIX_ENABLE("Prefix.Enable", true),
    FILE_NAME("FileName", "Necronic"),
    CURRENT_SERVER("CurrentServer", "Lobby"),
    PERMISSION_SPAWN("Permission.Spawn", "necronic.command.spawn"),
    PERMISSION_SET_SPAWN("Permission.SetSpawn", "necronic.command.setspawn"),
    PERMISSION_SPAWN_HEIGHT("Permission.SpawnHeight", "necronic.command.spawnheight"),
    PERMISSION_SET_SPAWN_HEIGHT("Permission.SetSpawnHeight", "necronic.command.setspawnheight"),
    PERMISSION_BUILD_COMMAND("Permission.Build.Command", "necronic.command.build.command"),
    PERMISSION_BUILD_USE("Permission.Build.Use", "necronic.command.build.use"),
    PERMISSION_BUILD_OTHER("Permission.Build.Other", "necronic.command.build.other"),
    PERMISSION_COLOR_NORMAL("Permission.Color.Normal", "necronic.event.color.normal"),
    PERMISSION_COLOR_HEX("Permission.Color.Hex", "necronic.event.color.hex"),
    PERMISSION_RANK_COMMAND("Permission.Rank.Command", "necronic.command.rank.command"),
    PERMISSION_RANK_ADD("Permission.Rank.Add", "necronic.command.rank.add"),
    PERMISSION_RANK_REMOVE("Permission.Rank.Remove", "necronic.command.rank.remove"),
    PERMISSION_RANK_GET("Permission.Rank.Get", "necronic.command.rank.get"),
    PERMISSION_RANK_SET_CHAT_PREFIX("Permission.Rank.Set.Chat.Prefix", "necronic.command.rank.set.chat.prefix"),
    PERMISSION_RANK_SET_CHAT_SUFFIX("Permission.Rank.Set.Chat.Suffix", "necronic.command.rank.set.chat.suffix"),
    PERMISSION_RANK_SET_CHAT_COLOR("Permission.Rank.Set.Chat.Color", "necronic.command.rank.set.chat.color"),
    PERMISSION_RANK_SET_TAB_PREFIX("Permission.Rank.Set.Tab.Prefix", "necronic.command.rank.set.tab.prefix"),
    PERMISSION_RANK_SET_TAB_SUFFIX("Permission.Rank.Set.Tab.Suffix", "necronic.command.rank.set.tab.suffix"),
    PERMISSION_RANK_SET_TAB_COLOR("Permission.Rank.Set.Tab.Color", "necronic.command.rank.set.tab.color"),
    PERMISSION_RANK_SET_TAB_ID("Permission.Rank.Set.Tab.ID", "necronic.command.rank.set.tab.id"),
    PERMISSION_RANK_SET_TEAM_ID("Permission.Rank.Set.TeamID", "necronic.command.rank.set.teamid"),
    PERMISSION_RANK_SET_PERMISSION("Permission.Rank.Set.Permission", "necronic.command.rank.set.permission"),
    COMMAND_ENABLE_SPAWN("Command.Enable.Spawn", true),
    COMMAND_ENABLE_SET_SPAWN("Command.Enable.SetSpawn", true),
    COMMAND_ENABLE_SPAWN_HEIGHT("Command.Enable.SpawnHeight", true),
    COMMAND_ENABLE_SET_SPAWN_HEIGHT("Command.Enable.SetSpawnHeight", true),
    COMMAND_ENABLE_BUILD_COMMAND("Command.Enable.Build.Command", true),
    COMMAND_ENABLE_BUILD_USE("Command.Enable.Build.Use", true),
    COMMAND_ENABLE_BUILD_OTHER("Command.Enable.Build.Other", true),
    COMMAND_ENABLE_RANK_COMMAND("Command.Enable.Rank.Command", true),
    COMMAND_ENABLE_RANK_ADD("Command.Enable.Rank.Add", true),
    COMMAND_ENABLE_RANK_REMOVE("Command.Enable.Rank.Remove", true),
    COMMAND_ENABLE_RANK_GET("Command.Enable.Rank.Get", true),
    COMMAND_ENABLE_RANK_SET_CHAT_PREFIX("Command.Enable.Rank.Set.Chat.Prefix", true),
    COMMAND_ENABLE_RANK_SET_CHAT_SUFFIX("Command.Enable.Rank.Set.Chat.Suffix", true),
    COMMAND_ENABLE_RANK_SET_CHAT_COLOR("Command.Enable.Rank.Set.Chat.Color", true),
    COMMAND_ENABLE_RANK_SET_TAB_PREFIX("Command.Enable.Rank.Set.Tab.Prefix", true),
    COMMAND_ENABLE_RANK_SET_TAB_SUFFIX("Command.Enable.Rank.Set.Tab.Suffix", true),
    COMMAND_ENABLE_RANK_SET_TAB_COLOR("Command.Enable.Rank.Set.Tab.Color", true),
    COMMAND_ENABLE_RANK_SET_TAB_ID("Command.Enable.Rank.Set.Tab.ID", true),
    COMMAND_ENABLE_RANK_SET_TEAM_ID("Command.Enable.Rank.Set.TeamID", true),
    COMMAND_ENABLE_RANK_SET_PERMISSION("Command.Enable.Rank.Set.Permission", true),
    EVENT_ENABLE_PLAYER_JOIN("Event.Enable.PlayerJoin", true),
    EVENT_ENABLE_PLAYER_QUIT("Event.Enable.PlayerQuit", true),
    EVENT_ENABLE_TELEPORT_TO_SPAWN("Event.Enable.TeleportToSpawn", true),
    EVENT_ENABLE_JOIN_MESSAGE("Event.Enable.Join.Message", true),
    EVENT_ENABLE_JOIN_TITLE("Event.Enable.Join.Title", true),
    EVENT_PROTECTED_FOOD_LEVEL_CHANGE("Event.Protected.FoodLevelChange", true),
    EVENT_PROTECTED_PLAYER_DROP_ITEM("Event.Protected.PlayerDropItem", true),
    EVENT_PROTECTED_PLAYER_PICKUP_ITEM("Event.Protected.PlayerPickupItem", true),
    EVENT_PROTECTED_ENTITY_EXPLODE("Event.Protected.EntityExplode", true),
    EVENT_PROTECTED_BLOCK_EXPLODE("Event.Protected.BlockExplode", true),
    EVENT_PROTECTED_PLAYER_CHANGE_LEVEL("Event.Protected.PlayerChange.Level", 2023),
    EVENT_PROTECTED_PLAYER_CHANGE_EXP("Event.Protected.PlayerChange.Exp", 1.0F / 12 * 6),
    RANK_CHAT_PREFIX_MESSAGE("Rank.Chat.PrefixMessage", " &8» &r"),
    RANK_UPDATE_TAB_TIMER("Rank.Update.TabTimer", 2L),
    RANK_PRE_PERMISSION("Rank.DefaultPrePermission", "necronic.rank."),
    RANK_DEFAULT_NAME("Rank.Default.Name", "default"),
    RANK_DEFAULT_CHAT_PREFIX("Rank.Default.Chat.Prefix", "&7"),
    RANK_DEFAULT_CHAT_SUFFIX("Rank.Default.Chat.Suffix", ""),
    RANK_DEFAULT_CHAT_COLOR("Rank.Default.Chat.Color", "&f"),
    RANK_DEFAULT_TAB_PREFIX("Rank.Default.Tab.Prefix", "&7"),
    RANK_DEFAULT_TAB_SUFFIX("Rank.Default.Tab.Suffix", ""),
    RANK_DEFAULT_TAB_COLOR("Rank.Default.Tab.Color", ChatColor.GRAY.toString()),
    RANK_DEFAULT_TAB_ID("Rank.Default.Tab.ID", "1111"),
    RANK_LIVE_SUFFIX("Rank.Live.Suffix", " &8[&5LIVE&8]"),
    RANK_ENABLE_TAB("Rank.Enable", true),
    SCOREBOARD_ENABLE_TAB_LIST("Scoreboard.Enable.TabList", true),
    SCOREBOARD_UPDATE_TAB_LIST("Scoreboard.Update.TabList", 2L),
    ;

    private final String path;
    private final Object value;

    Configs(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }
}
