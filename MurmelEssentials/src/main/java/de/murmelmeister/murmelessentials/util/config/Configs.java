package de.murmelmeister.murmelessentials.util.config;

public enum Configs {
    PREFIX_ENABLE("Prefix.Enable", true),
    FILE_NAME("FileName", "MurmelEssentials"),
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
