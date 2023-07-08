package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cooldown {
    private final Logger logger;

    private File file;
    private YamlConfiguration config;

    public Cooldown(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
    }

    public void create(Player player) {
        String fileName = player.getUniqueId() + ".yml";
        this.file = new File("plugins//NetherLegends//Cooldown//", fileName);
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
        String created = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(new Date());
        String expired = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(duration);
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
