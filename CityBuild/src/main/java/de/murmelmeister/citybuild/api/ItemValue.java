package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.ConfigUtil;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class ItemValue {
    private final Logger logger;
    private final Config defaultConfig;
    private final Economy economy;

    private File file;
    private YamlConfiguration config;

    public ItemValue(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.defaultConfig = main.getConfig();
        this.economy = main.getEconomy();
    }

    public void register() {
        create();
        load();
        save();
    }

    public void create() {
        String fileName = "itemValue.yml";
        this.file = new File("plugins//NetherLegends//Economy//", fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        for (Material material : Material.values())
            if (get(material.name() + ".Value") == null) set(material.name() + ".Value", defaultSellPrice());
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long defaultSellPrice() {
        return defaultConfig.getLong(Configs.ECONOMY_DEFAULT_SELL_PRICE);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    private Object get(String path) {
        return config.get(path);
    }

    public Object getValue(Material material) {
        return config.get(material.name() + ".Value");
    }
}
