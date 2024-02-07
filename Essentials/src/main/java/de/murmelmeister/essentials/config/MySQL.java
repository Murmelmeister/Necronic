package de.murmelmeister.essentials.config;

import com.moandjiezana.toml.Toml;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class MySQL {
    private final Logger logger;
    private File file;
    private Toml toml;
    private HikariDataSource dataSource;

    public MySQL(Logger logger) {
        this.logger = logger;
        load();
    }

    private void load() {
        this.file =  new File("plugins//Essentials//", "/mysql.toml");
        if (!file.getParentFile().exists()) {
            boolean b = file.getParentFile().mkdirs();
            if (!b) logger.info("Cloud not maje a new mysql.toml file.");
        }
        if (!file.exists()) {
            try {
                Files.copy(Objects.requireNonNull(MySQL.class.getResourceAsStream("/mysql.toml")), file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        toml = new Toml().read(file);
    }

    public void initConnectionPool() {
        if (!file.exists()) throw new RuntimeException("The MySQL-Config file does not exist.");
        this.dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s?autoReconnect=true&useUnicode=yes", read("MySQL.Driver").toLowerCase(), read("MySQL.Hostname"), read("MySQL.Port"), read("MySQL.Database")));
        dataSource.setUsername(read("MySQL.Username"));
        dataSource.setPassword(read("MySQL.Password"));
    }

    public void closeConnectionPool() {
        dataSource.close();
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    private String read(String path) {
        return toml.getString(path);
    }
}
