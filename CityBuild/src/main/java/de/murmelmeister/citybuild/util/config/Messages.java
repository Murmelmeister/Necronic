package de.murmelmeister.citybuild.util.config;

public enum Messages {

    PREFIX("Prefix", "&6NetherLegends &8» &r"),
    NO_PERMISSION("NoPermission", "&cYou don't have the permission to do that."),
    NO_CONSOLE("NoConsole", "&cThis command does not work in the console."),
    NO_PLAYER_EXIST("NoPlayerExist", "&cThis player &e[PLAYER] &cis not online on the server."),
    DISABLE_COMMAND("DisableCommand", "&cThe command is disabled."),
    COMMAND_ENDER_CHEST_SYNTAX("Command.EnderChest.Syntax", "&7Syntax: &c[USAGE]"),
    COMMAND_RELOAD("Command.Reload", "&aThe configurations have been reloaded.");

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
