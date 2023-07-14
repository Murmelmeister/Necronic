package de.murmelmeister.lobby.util.config;

public enum Messages {

    PREFIX("Prefix", "&5Necronic &8» &r"),
    NO_PERMISSION("NoPermission", "&cYou don't have the permission to do that."),
    NO_CONSOLE("NoConsole", "&cThis command does not work in the console."),
    NO_PLAYER_EXIST("NoPlayerExist", "&7This player &e[PLAYER] &7is&c not&7 on the&c server&7."),
    COMMAND_SYNTAX("Command.Syntax", "&7Syntax: &c[USAGE]"),
    DISABLE_COMMAND("DisableCommand", "&cThe command is disabled.");

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
