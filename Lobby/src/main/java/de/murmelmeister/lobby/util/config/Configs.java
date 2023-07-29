package de.murmelmeister.lobby.util.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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
    COMMAND_ENABLE_SPAWN("Command.Enable.Spawn", true),
    COMMAND_ENABLE_SET_SPAWN("Command.Enable.SetSpawn", true),
    COMMAND_ENABLE_SPAWN_HEIGHT("Command.Enable.SpawnHeight", true),
    COMMAND_ENABLE_SET_SPAWN_HEIGHT("Command.Enable.SetSpawnHeight", true),
    COMMAND_ENABLE_BUILD_COMMAND("Command.Enable.Build.Command", true),
    COMMAND_ENABLE_BUILD_USE("Command.Enable.Build.Use", true),
    COMMAND_ENABLE_BUILD_OTHER("Command.Enable.Build.Other", true),
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
    EVENT_PROTECTED_ATTACK_DAMAGE("Event.Protected.AttackDamage", true),
    EVENT_PROTECTED_FALL_DAMAGE("Event.Protected.FallDamage", true),
    EVENT_PROTECTED_PLAYER_CHANGE_LEVEL("Event.Protected.PlayerChange.Level", 2023),
    EVENT_PROTECTED_PLAYER_CHANGE_EXP("Event.Protected.PlayerChange.Exp", 1.0F / 12 * 6),
    EVENT_LOBBY_ITEMS_CITY_BUILD("Event.LobbyItems.CityBuild", "CityBuild"),
    SCOREBOARD_ENABLE_TAB_LIST("Scoreboard.Enable.TabList", true),
    SCOREBOARD_UPDATE_TAB_LIST("Scoreboard.Update.TabList", 2L),
    LOBBY_ITEMS_NAVIGATOR_MATERIAL("Lobby.Items.Navigator.Material", Material.RECOVERY_COMPASS.name()),
    LOBBY_ITEMS_NAVIGATOR_DISPLAY_NAME("Lobby.Items.Navigator.DisplayName", "&8» &5Navigator &8« &7(RIGHT CLICK)"),
    LOBBY_ITEMS_NAVIGATOR_INVENTORY_NAME("Lobby.Items.Navigator.InventoryName", "&5Navigator"),
    LOBBY_ITEMS_NAVIGATOR_SLOT("Lobby.Items.Navigator.Slot", 40),
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
