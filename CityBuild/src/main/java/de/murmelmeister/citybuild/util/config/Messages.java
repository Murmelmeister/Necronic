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
    COMMAND_SEND_LOCATION("Command.Warp.Send", "&aYou have teleport to the &e[LOCATION]&a."),
    COMMAND_SET_LOCATION("Command.Warp.Set", "&aYou have set the &e[LOCATION]&a."),
    COMMAND_REMOVE_LOCATION("Command.Warp.remove", "&aYou have remove the &e[LOCATION]&a."),
    COMMAND_SEND_HOME("Command.Home.Send", "&aYou have teleported to your home &e[HOME]&a."),
    COMMAND_ADD_HOME("Command.Home.Add", "&aYou have set your home &e[HOME]&a."),
    COMMAND_REMOVE_HOME("Command.Home.Remove", "&aYou have deleted your home &e[HOME]&a."),
    COMMAND_EXIST_HOME("Command.Home.Exist", "&cThe home does exist. Please delete '&e[HOME]&c'."),
    COMMAND_NOT_EXIST_HOME("Command.Home.NotExist", "&cThe home &e[HOME]&c does not exist."),
    COMMAND_HOME_LIMIT("Command.Home.Limit", "&cYou have the max homes."),
    COMMAND_FEED_USE("Command.Feed.Use", "&aYour hunger was satisfied."),
    COMMAND_FEED_OTHER("Command.Feed.Other", "&aThe player&e [PLAYER]&a his hunger was satisfied."),
    COMMAND_HEAL_USE("Command.Heal.Use", "&aYou have been&e healed&a."),
    COMMAND_HEAL_OTHER("Command.Heal.Other", "&aThe player&e [PLAYER]&a has been&e healed&a."),
    COMMAND_FLY_USE_ON("Command.Fly.Use.On", "&aYou have been put into&e fly mode&a."),
    COMMAND_FLY_USE_OFF("Command.Fly.Use.Off", "&cYou were put out of&e fly mode&c."),
    COMMAND_FLY_OTHER_ON("Command.Fly.Other.On", "&aThe player&e [PLAYER]&a has been put into&e fly mode&a."),
    COMMAND_FLY_OTHER_OFF("Command.Fly.Other.Off", "&cThe player&e [PLAYER]&c were put out of&e fly mode&c."),
    COMMAND_GOD_MODE_USE_ON("Command.GodMode.Use.On", "&aYou have been put into&e god mode&a."),
    COMMAND_GOD_MODE_USE_OFF("Command.GodMode.Use.Off", "&cYou were put out of&e god mode&c."),
    COMMAND_GOD_MODE_OTHER_ON("Command.GodMode.Other.On", "&aThe player&e [PLAYER]&a has been put into&e god mode&a."),
    COMMAND_GOD_MODE_OTHER_OFF("Command.GodMode.Other.Off", "&cThe player&e [PLAYER]&c were put out of&e god mode&c."),
    EVENT_PLAYER_JOIN("Event.PlayerJoin", "&e[PLAYER]&7 joined the game."),
    EVENT_PLAYER_QUIT("Event.PlayerQuit", "&e[PLAYER]&7 left the game."),
    EVENT_SPAWN_NOT_EXIST("Event.SpawnNotExist", "&8--- &cWARNING &8---\n[PREFIX]&cThe &eSpawn&c does not exist."),
    EVENT_JOIN_AUTO_FLY("Event.Join.Auto.Fly", "&aYou have been automatically put in&e fly mode&a."),
    EVENT_JOIN_AUTO_GOD_MODE("Event.Join.Auto.GodMode", "&aYou have been automatically put in&e god mode&a."),
    INVENTORY_TRASH("Inventory.Trash", "&cTrash");

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
