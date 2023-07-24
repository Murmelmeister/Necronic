package de.murmelmeister.essentials.util;

public enum MySQLConf {
    MYSQL_HOSTNAME("Hostname", "localhost"),
    MYSQL_PORT("Port", 3306),
    MYSQL_DATABASE("Database", "essentials"),
    MYSQL_USERNAME("Username", "user"),
    MYSQL_PASSWORD("Password", "12345");
    private final String path;
    private final Object value;

    MySQLConf(String path, Object value) {
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
