package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Cooldown {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;

    public Cooldown(Main main) {
        this.logger = main.getLogger();
        this.defaultConfig = main.getConfig();
    }

    public void create(UUID uuid) {
        String fileName = uuid + ".yml";
        this.file = new File(String.format("plugins//%s//Cooldown//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setUsername(UUID uuid, String name) {
        create(uuid);
        set("Username", name);
        save();
    }

    public void addCooldown(UUID uuid, String cooldownName, long time) {
        create(uuid);
        long duration = time + System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(defaultConfig.getString(Configs.PATTERN_CONFIG));
        String created = format.format(new Date());
        String expired = format.format(duration);
        String path = "Cooldown." + cooldownName;
        set(path + ".Name", cooldownName);
        set(path + ".Duration", duration);
        set(path + ".Created", created);
        set(path + ".Expired", expired);
        save();
    }

    public void removeCooldown(UUID uuid, String cooldownName) {
        create(uuid);
        set("Cooldown." + cooldownName, null);
        save();
    }

    public boolean hasCooldown(UUID uuid, String cooldownName) {
        create(uuid);
        return config.get("Cooldown." + cooldownName) != null;
    }

    public long getDuration(UUID uuid, String cooldownName) {
        create(uuid);
        return config.getLong("Cooldown." + cooldownName + ".Duration");
    }

    public String getExpired(UUID uuid, String cooldownName) {
        create(uuid);
        return config.getString("Cooldown." + cooldownName + ".Expired");
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }
}
