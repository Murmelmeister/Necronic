package de.murmelmeister.essentials.util;

public enum MySQLConf {
    MYSQL_HOSTNAME("MySQL.Hostname", "localhost"),
    MYSQL_PORT("MySQL.Port", "3306"),
    MYSQL_DATABASE("MySQL.Database", "EssentialsBase"),
    MYSQL_USERNAME("MySQL.Username", "user"),
    MYSQL_PASSWORD("MySQL.Password", "12345");
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
