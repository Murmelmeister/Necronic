package de.murmelmeister.lobby.util.config;

public enum Configs {

    PREFIX_ENABLE("Prefix.Enable", true),
    FILE_NAME("FileName", "Necronic"),
    CURRENT_SERVER("CurrentServer", "Lobby"),
    PERMISSION_SPAWN("Permission.Spawn", "necronic.command.spawn"),
    PERMISSION_SET_SPAWN("Permission.SetSpawn", "necronic.command.setspawn"),
    PERMISSION_BUILD_COMMAND("Permission.Build.Command", "necronic.command.build.command"),
    PERMISSION_BUILD_USE("Permission.Build.Use", "necronic.command.build.use"),
    PERMISSION_BUILD_OTHER("Permission.Build.Other", "necronic.command.build.other"),
    PERMISSION_COLOR_NORMAL("Permission.Color.Normal", "necronic.event.color.normal"),
    PERMISSION_COLOR_HEX("Permission.Color.Hex", "necronic.event.color.hex"),
    COMMAND_ENABLE_SPAWN("Command.Enable.Spawn", true),
    COMMAND_ENABLE_SET_SPAWN("Command.Enable.SetSpawn", true),
    COMMAND_BUILD_COMMAND("Command.Build.Command", true),
    COMMAND_BUILD_USE("Command.Build.Use", true),
    COMMAND_BUILD_OTHER("Command.Build.Other", true),
    EVENT_ENABLE_PLAYER_JOIN("Event.Enable.PlayerJoin", true),
    EVENT_ENABLE_PLAYER_QUIT("Event.Enable.PlayerQuit", true),
    EVENT_ENABLE_TELEPORT_TO_SPAWN("Event.Enable.TeleportToSpawn", true),
    EVENT_ENABLE_JOIN_MESSAGE("Event.Enable.Join.Message", true),
    EVENT_ENABLE_JOIN_TITLE("Event.Enable.Join.Title", true);

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
