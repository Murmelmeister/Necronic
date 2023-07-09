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

public class Economy {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;

    public Economy(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.defaultConfig = main.getConfig();
    }

    public void create(Player player) {
        String fileName = player.getUniqueId() + ".yml";
        this.file = new File("plugins//NetherLegends//Economy//UserData//", fileName);
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

    public void setUsername(Player player) {
        create(player);
        set("Username", player.getName());
        save();
    }

    public double defaultMoney() {
        return defaultConfig.getDouble(Configs.ECONOMY_DEFAULT_MONEY);
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }
}
