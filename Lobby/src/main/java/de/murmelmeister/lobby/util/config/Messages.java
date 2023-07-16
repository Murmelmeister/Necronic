package de.murmelmeister.lobby.util.config;

public enum Messages {

    PREFIX("Prefix", "&5Necronic &8» &r"),
    NO_PERMISSION("NoPermission", "&cYou don't have the permission to do that."),
    NO_CONSOLE("NoConsole", "&cThis command does not work in the console."),
    NO_PLAYER_EXIST("NoPlayerExist", "&7This player &e[PLAYER] &7is&c not&7 on the&c server&7."),
    COMMAND_SYNTAX("Command.Syntax", "&7Syntax: &c[USAGE]"),
    DISABLE_COMMAND("DisableCommand", "&cThe command is disabled."),
    COMMAND_LOCATION_NOT_EXIST("Command.LocationNotExist", "&7The location &e[LOCATION]&7 does not&c exist&7."),
    COMMAND_SEND_SPAWN("Command.Spawn.Send", "&7You have&a teleport&7 to the&e Spawn&7."),
    COMMAND_SET_SPAWN("Command.Spawn.Set", "&7You have&a set&7 the&e Spawn&7.");

    private final String path;
    private final String value;

    Messages(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
        return value;
    }
}
