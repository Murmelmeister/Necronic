package de.murmelmeister.citybuild.util.config;

public enum Configs {

    PREFIX_ENABLE("Prefix.Enable", true),
    COMMAND_ENABLE_ENDER_CHEST_COMMAND("Command.Enable.EnderChest.Command", true),
    COMMAND_ENABLE_ENDER_CHEST_USE("Command.Enable.EnderChest.Use", true),
    COMMAND_ENABLE_ENDER_CHEST_OTHER("Command.Enable.EnderChest.Other", true),
    COMMAND_ENABLE_RELOAD("Command.Enable.Reload", true),
    PERMISSION_ENDER_CHEST_COMMAND("Permission.EnderChest.Command", "netherlegends.command.enderchest.command"),
    PERMISSION_ENDER_CHEST_USE("Permission.EnderChest.Use", "netherlegends.command.enderchest.use"),
    PERMISSION_ENDER_CHEST_OTHER("Permission.EnderChest.Other", "netherlegends.command.enderchest.other"),
    PERMISSION_RELOAD("Permission.Reload", "netherlegends.command.reload");

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
