package de.murmelmeister.murmelessentials.util.config;

public enum Configs {
    PREFIX_ENABLE("Prefix.Enable", true),
    FILE_NAME("FileName", "MurmelEssentials"),
    DEFAULT_GROUP("Default.Group", "default"),
    UPDATE_SCHEDULE_PERMISSION_COMMAND("Update.Schedule.PermissionCommand", 5L),
    UPDATE_SCHEDULE_RANK_PERMISSION("Update.Schedule.RankPermission", 30L),
    UPDATE_SCHEDULE_PLAYER_LIST("Update.Schedule.PlayerList", 2L),
    UPDATE_SCHEDULE_TAB_LIST("Update.Schedule.TabList", 2L),
    DEFAULT_PRE_PERMISSION("Default.PrePermission", "murmelessentials.permission."),
    DEFAULT_CHAT_PREFIX_MESSAGE("Default.Chat.PrefixMessage", " &8» &r"),
    PERMISSION_COLOR_NORMAL("Permission.Color.Normal", "murmelessentials.color.normal"),
    PERMISSION_COLOR_HEX("Permission.Color.Hex", "murmelessentials.color.hex"),
    PERMISSION_RELOAD("Permission.Reload", "murmelessentials.command.reload"),
    COMMAND_ENABLE_RELOAD("Command.Enable.Reload", true),
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
