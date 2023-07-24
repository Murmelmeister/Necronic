package de.murmelmeister.essentials.config;

import de.murmelmeister.essentials.util.MySQLConf;
import de.murmelmeister.murmelapi.configuration.MurmelConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private File file;
    private MurmelConfiguration config;
    private Connection connection;

    public void register() {
        create();
        load();
        save();
    }

    public void create() {
        String fileName = "mysql.murmel";
        this.file = new File("plugins//essentials//", fileName);
        MurmelConfiguration.createFile(file, fileName);
        this.config = MurmelConfiguration.decodedLoadConfiguration(file);
    }

    public void load() {
        for (MySQLConf mySQLConf : MySQLConf.values()) if (get(mySQLConf) == null) set(mySQLConf);
    }

    public void save() {
        try {
            config.decodedSave(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connected() {
        if (!(isConnected()))
            try {
                this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true&useUnicode=yes", get(MySQLConf.MYSQL_HOSTNAME), get(MySQLConf.MYSQL_PORT), get(MySQLConf.MYSQL_DATABASE)), get(MySQLConf.MYSQL_USERNAME).toString(), get(MySQLConf.MYSQL_PASSWORD).toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public void disconnected() {
        if (isConnected())
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    private void set(MySQLConf mySQLConf) {
        config.set(mySQLConf.getPath(), mySQLConf.getValue());
    }

    private Object get(MySQLConf mySQLConf) {
        return config.get(mySQLConf.getPath());
    }

    public Connection getConnection() {
        return connection;
    }

    private boolean isConnected() {
        return connection != null;
    }
}
