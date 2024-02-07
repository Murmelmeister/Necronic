package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.murmelapi.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.UUID;

public class Economy {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;

    public Economy(Main main) {
        this.logger = main.getLogger();
        this.defaultConfig = main.getConfig();
    }

    public void create(UUID uuid) {
        String fileName = uuid + ".yml";
        this.file = new File(String.format("plugins//%s//Economy//UserData//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
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
        if (config.getString("Money") == null) set("Money", defaultMoney());
        save();
    }

    public BigDecimal getMoney(UUID uuid) {
        create(uuid);
        BigDecimal exactMoney = defaultMoney();
        double money = exactMoney.doubleValue();
        new BigDecimal(money);
        money = config.getDouble("Money");
        exactMoney = BigDecimal.valueOf(money);
        return exactMoney;
    }

    public void setMoney(UUID uuid, BigDecimal money) {
        create(uuid);
        set("Money", money);
        save();
    }

    public void addMoney(UUID uuid, BigDecimal money) {
        create(uuid);
        BigDecimal amount = getMoney(uuid).add(money, MathContext.DECIMAL128);
        set("Money", amount);
        save();
    }

    public void removeMoney(UUID uuid, BigDecimal money) {
        create(uuid);
        BigDecimal amount = getMoney(uuid).subtract(money, MathContext.DECIMAL128);
        set("Money", amount);
        save();
    }

    public void resetMoney(UUID uuid) {
        create(uuid);
        set("Money", defaultMoney());
        save();
    }

    public void payMoney(UUID player, UUID target, BigDecimal money) {
        removeMoney(player, money);
        addMoney(target, money);
    }

    public boolean hasEnoughMoney(UUID uuid, double money) {
        create(uuid);
        return money <= getMoney(uuid).doubleValue();
    }

    public BigDecimal defaultMoney() {
        return BigDecimal.valueOf(defaultConfig.getDouble(Configs.ECONOMY_DEFAULT_MONEY));
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }
}
