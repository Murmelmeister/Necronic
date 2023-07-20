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
    COMMAND_SET_SPAWN("Command.Spawn.Set", "&7You have&a set&7 the&e Spawn&7."),
    COMMAND_GET_SPAWN_HEIGHT("Command.SpawnHeight.Get", "&7The Spawn height is&a [HEIGHT]&7."),
    COMMAND_SET_SPAWN_HEIGHT("Command.SpawnHeight.Set", "&7You have&a set&7 the&e Spawn height&7. &8(&7Height: &e[HEIGHT]&8)"),
    COMMAND_EXIST_SPAWN_HEIGHT("Command.SpawnHeight.Exist", "&7The&e Spawn height&7 does not&c exist&7."),
    EVENT_PLAYER_JOIN("Event.PlayerJoin", "&d[PLAYER]&7 joined the game."),
    EVENT_PLAYER_QUIT("Event.PlayerQuit", "&d[PLAYER]&7 left the game."),
    EVENT_SPAWN_NOT_EXIST("Event.SpawnNotExist", "&8--- &cWARNING &8---\n[PREFIX]&cThe &eSpawn&c does not exist."),
    EVENT_JOIN_MESSAGE("Event.Join.Message", "&8» &dNeconic.net \n&8»&7 Welcome on citybuild \n&8»&7 Discord&8:&d /discord"),
    EVENT_JOIN_TITLE("Event.Join.Title", "&7Hey&8, &5[PLAYER]"),
    EVENT_JOIN_SUB_TITLE("Event.Join.SubTitle", "&7Have fun on the&d CityBuild");

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
