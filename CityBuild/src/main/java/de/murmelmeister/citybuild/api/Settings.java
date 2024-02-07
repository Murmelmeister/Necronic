package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Settings {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;

    public Settings(Main main) {
        this.logger = main.getLogger();
        this.defaultConfig = main.getConfig();
    }

    public void create(UUID uuid) {
        String fileName = uuid + ".yml";
        this.file = new File(String.format("plugins//%s//Settings//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsername(UUID uuid, String name) {
        create(uuid);
        set("Username", name);
        save();
    }

    public void createAccount(UUID uuid) {
        create(uuid);
        if (get("Settings.Scoreboard") == null) set("Settings.Scoreboard", true);
        save();
    }

    public void setEnableScoreboard(UUID uuid, boolean mode) {
        createAccount(uuid);
        set("Settings.Scoreboard", mode);
        save();
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    private Object get(String path) {
        return config.get(path);
    }

    private boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public boolean getBoolean(UUID uuid, String path) {
        createAccount(uuid);
        return getBoolean(path);
    }
}
