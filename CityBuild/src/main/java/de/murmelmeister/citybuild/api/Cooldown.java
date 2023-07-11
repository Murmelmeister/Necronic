package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.ConfigUtil;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Cooldown {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;

    public Cooldown(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.defaultConfig = main.getConfig();
    }

    public void create(Player player) {
        String fileName = player.getUniqueId() + ".yml";
        this.file = new File(String.format("plugins//%s//Cooldown//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setUsername(Player player) {
        create(player);
        set("Username", player.getName());
        save();
    }

    public void addCooldown(Player player, String cooldownName, long time) {
        create(player);
        long duration = time + System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(defaultConfig.getString(Configs.PATTERN_CONFIG));
        if (defaultConfig.getBoolean(Configs.TIMEZONE_CONFIG_ENABLE)) format.setTimeZone(TimeZone.getTimeZone(defaultConfig.getString(Configs.TIMEZONE_ZONE)));
        String created = format.format(new Date());
        String expired = format.format(duration); // TimeZone UTC
        String path = "Cooldown." + cooldownName;
        set(path + ".Name", cooldownName);
        set(path + ".Duration", duration);
        set(path + ".Created", created);
        set(path + ".Expired", expired);
        save();
    }

    public void removeCooldown(Player player, String cooldownName) {
        create(player);
        set("Cooldown." + cooldownName, null);
        save();
    }

    public boolean hasCooldown(Player player, String cooldownName) {
        create(player);
        return config.get("Cooldown." + cooldownName) != null;
    }

    public long getDuration(Player player, String cooldownName) {
        create(player);
        return config.getLong("Cooldown." + cooldownName + ".Duration");
    }

    public String getExpired(Player player, String cooldownName) {
        create(player);
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
