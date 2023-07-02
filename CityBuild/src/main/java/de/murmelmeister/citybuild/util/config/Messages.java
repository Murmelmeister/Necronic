package de.murmelmeister.citybuild.util.config;

public enum Messages {

    PREFIX("Prefix", "&6NetherLegends &8» &r"),
    NO_PERMISSION("NoPermission", "&cYou don't have the permission to do that."),
    NO_CONSOLE("NoConsole", "&cThis command does not work in the console."),
    NO_PLAYER_EXIST("NoPlayerExist", "&cThis player &e[PLAYER] &cis not online on the server."),
    COMMAND_SYNTAX("Command.Syntax", "&7Syntax: &c[USAGE]"),
    DISABLE_COMMAND("DisableCommand", "&cThe command is disabled."),
    COMMAND_RELOAD("Command.Reload", "&aThe configurations have been reloaded."),
    COMMAND_LOCATION_NOT_EXIST("Command.LocationNotExist", "&cThe location &e[LOCATION]&c does not exist."),
    COMMAND_SEND_SPAWN("Command.Spawn.Send", "&aYou have teleport to the&e Spawn&a."),
    COMMAND_SET_SPAWN("Command.Spawn.Set", "&aYou have set the&e Spawn&a."),
    COMMAND_SEND_LOCATION("Command.Warp.Send", "&aYou have teleport to the&e [LOCATION]&a."),
    COMMAND_SET_LOCATION("Command.Warp.Set", "&aYou have set the&e [LOCATION]&a."),
    COMMAND_REMOVE_LOCATION("Command.Warp.remove", "&aYou have remove the&e [LOCATION]&a."),
    EVENT_PLAYER_JOIN("Event.PlayerJoin", "&e[PLAYER]&7 joined the game."),
    EVENT_PLAYER_QUIT("Event.PlayerQuit", "&e[PLAYER]&7 left the game."),
    EVENT_SPAWN_NOT_EXIST("Event.SpawnNotExist", "&8--- &cWARNING &8---\n[PREFIX]&cThe &eSpawn&c does not exist.");

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
