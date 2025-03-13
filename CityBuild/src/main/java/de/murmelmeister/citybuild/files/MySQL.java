package de.murmelmeister.citybuild.files;

import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.MurmelAPI;
import de.murmelmeister.murmelapi.utils.FileUtil;
import org.slf4j.Logger;

import java.io.File;

public final class MySQL {
    private final File file;

    public MySQL(Logger logger, ConfigFile configFile) {
        this.file = FileUtil.createFile(logger, configFile.getString(Configs.MURMEL_API_CONFIG_PATH), configFile.getString(Configs.MURMEL_API_CONFIG_FILE));
    }

    public void connect() {
        var properties = FileUtil.loadProperties(file);
        String databaseName = properties.getProperty("DB_DATABASE");
        MurmelAPI.setDatabaseName(databaseName);
        String url = "jdbc:" + properties.getProperty("DB_DRIVER") + "://" + properties.getProperty("DB_HOSTNAME") + ":" + properties.getProperty("DB_PORT") + "/" + databaseName;
        MurmelAPI.connect(url, properties.getProperty("DB_USERNAME"), properties.getProperty("DB_PASSWORD"));
    }

    public void disconnect() {
        MurmelAPI.disconnect();
    }
}
