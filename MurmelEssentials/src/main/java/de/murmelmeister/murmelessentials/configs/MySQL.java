package de.murmelmeister.murmelessentials.configs;

import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.util.ConfigUtil;
import de.murmelmeister.murmelessentials.util.config.Configs;
import de.murmelmeister.murmelessentials.util.config.MySQLs;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;
    private Connection connection;

    public MySQL(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.defaultConfig = main.getConfig();
    }

    public void register() {
        create();
        load();
        save();
    }

    public void create() {
        String fileName = "mysql.yml";
        this.file = new File(String.format("plugins//%s//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        for (MySQLs mySQLs : MySQLs.values()) if (get(mySQLs) == null) set(mySQLs);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connected() {
        if (connection != null) return;
        try {
            this.connection = DriverManager.getConnection(String.format("jdbc:%s://%s:%s/%s?autoReconnect=true&useUnicode=yes",
                    getString(MySQLs.MYSQL_DRIVER).toLowerCase(), getString(MySQLs.MYSQL_HOSTNAME), getString(MySQLs.MYSQL_PORT), getString(MySQLs.MYSQL_DATABASE)), getString(MySQLs.MYSQL_USERNAME), getString(MySQLs.MYSQL_PASSWORD));
            logger.info("MySQL connected.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnected() {
        if (connection == null) return;
        try {
            connection.close();
            logger.info("MySQL disconnected.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void set(MySQLs mySQLs) {
        config.set(mySQLs.getPath(), mySQLs.getValue());
    }

    private Object get(MySQLs mySQLs) {
        return config.get(mySQLs.getPath());
    }

    private String getString(MySQLs mySQLs) {
        return config.getString(mySQLs.getPath());
    }

    public Connection getConnection() {
        return connection;
    }
}
