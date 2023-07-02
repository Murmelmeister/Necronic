package de.murmelmeister.citybuild.util.config;

public enum Configs {

    PREFIX_ENABLE("Prefix.Enable", true);

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
