package de.murmelmeister.citybuild.util.config;

public enum Configs {

    PREFIX_ENABLE("Prefix.Enable", true),
    PERMISSION_ENDER_CHEST_COMMAND("Permission.EnderChest.Command", "netherlegends.command.enderchest.command"),
    PERMISSION_ENDER_CHEST_USE("Permission.EnderChest.Use", "netherlegends.command.enderchest.use"),
    PERMISSION_ENDER_CHEST_OTHER("Permission.EnderChest.Other", "netherlegends.command.enderchest.other"),
    PERMISSION_RELOAD("Permission.Reload", "netherlegends.command.reload"),
    PERMISSION_SPAWN("Permission.Spawn", "netherlegends.command.spawn"),
    PERMISSION_SET_SPAWN("Permission.SetSpawn", "netherlegends.command.setspawn"),
    PERMISSION_LOCATION("Permission.Warp", "netherlegends.command.warp"),
    PERMISSION_SET_LOCATION("Permission.SetWarp", "netherlegends.command.setwarp"),
    PERMISSION_REMOVE_LOCATION("Permission.RemoveWarp", "netherlegends.command.removewarp"),
    PERMISSION_HOME("Permission.Home", "netherlegends.command.home"),
    PERMISSION_ADD_HOME("Permission.AddHome", "netherlegends.command.addhome"),
    PERMISSION_REMOVE_HOME("Permission.RemoveHome", "netherlegends.command.removehome"),
    PERMISSION_COLOR_NORMAL("Permission.Color.Normal", "netherlegends.event.color.normal"),
    PERMISSION_COLOR_HEX("Permission.Color.Hex", "netherlegends.event.color.hex"),
    PERMISSION_HOME_UNLIMITED("Permission.Homes.Unlimited", "netherlegends.home.unlimited"),
    PERMISSION_HOME_LIMIT_TEAM("Permission.Homes.Limit.Team", "netherlegends.home.limit.team"),
    PERMISSION_HOME_LIMIT_RANK("Permission.Homes.Limit.Rank", "netherlegends.home.limit.rank"),
    COMMAND_ENABLE_ENDER_CHEST_COMMAND("Command.Enable.EnderChest.Command", true),
    COMMAND_ENABLE_ENDER_CHEST_USE("Command.Enable.EnderChest.Use", true),
    COMMAND_ENABLE_ENDER_CHEST_OTHER("Command.Enable.EnderChest.Other", true),
    COMMAND_ENABLE_RELOAD("Command.Enable.Reload", true),
    COMMAND_ENABLE_SPAWN("Command.Enable.Spawn", true),
    COMMAND_ENABLE_SET_SPAWN("Command.Enable.SetSpawn", true),
    COMMAND_ENABLE_LOCATION("Command.Enable.Warp", true),
    COMMAND_ENABLE_SET_LOCATION("Command.Enable.SetWarp", true),
    COMMAND_ENABLE_REMOVE_LOCATION("Command.Enable.RemoveWarp", true),
    COMMAND_ENABLE_HOME("Command.Enable.Home", true),
    COMMAND_ENABLE_ADD_HOME("Command.Enable.AddHome", true),
    COMMAND_ENABLE_REMOVE_HOME("Command.Enable.RemoveHome", true),
    EVENT_PLAYER_JOIN("Event.PlayerJoin", true),
    EVENT_PLAYER_QUIT("Event.PlayerQuit", true),
    EVENT_TELEPORT_TO_SPAWN("Event.TeleportToSpawn", true),
    HOME_LIMIT_DEFAULT("Home.Limit.Default", 4),
    HOME_LIMIT_RANK("Home.Limit.Rank", 8),
    HOME_LIMIT_TEAM("Home.Limit.Team", 12);

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
