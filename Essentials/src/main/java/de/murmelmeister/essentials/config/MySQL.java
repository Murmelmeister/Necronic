package de.murmelmeister.essentials.config;

import com.moandjiezana.toml.Toml;
import de.murmelmeister.essentials.util.MySQLConf;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MySQL {
    private final Logger logger;
    private final File file;
    private Toml toml;
    private Connection connection;

    public MySQL(Logger logger) {
        this.logger = logger;
        this.file = new File("plugins//essentials//", "/mysql.toml");
        create();
    }

    private void save() {
        if (!(file.getParentFile().exists())) {
            boolean a = file.getParentFile().mkdirs();
            if (!(a)) logger.info("Cloud not maje a new mysql.toml file.");
        }

        if (!(file.exists())) {
            try {
                Files.copy(Objects.requireNonNull(MySQL.class.getResourceAsStream("/mysql.toml")), file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void create() {
        save();
        toml = new Toml().read(file);
        for (MySQLConf mySQLConf : MySQLConf.values()) read(mySQLConf);
        toml.getString("Created");
    }

    private void read(MySQLConf mySQLConf) {
        toml.getString(mySQLConf.getPath());
    }

    private String getString(MySQLConf mySQLConf) {
        return toml.getString(mySQLConf.getPath());
    }

    public void connected() {
        if (connection != null) return;
        try {
            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true&useUnicode=yes",
                    getString(MySQLConf.MYSQL_HOSTNAME), getString(MySQLConf.MYSQL_PORT), getString(MySQLConf.MYSQL_DATABASE)), getString(MySQLConf.MYSQL_USERNAME), getString(MySQLConf.MYSQL_PASSWORD));
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

    public Connection getConnection() {
        return connection;
    }
}
