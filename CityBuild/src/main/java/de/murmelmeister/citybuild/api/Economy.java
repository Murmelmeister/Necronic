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
import java.math.BigDecimal;
import java.math.MathContext;

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

    public void setUsername(Player player) {
        create(player);
        set("Username", player.getName());
        save();
    }

    public void createAccount(Player player) {
        create(player);
        if (config.getString("Money") == null) set("Money", defaultMoney());
        save();
    }

    public BigDecimal getMoney(Player player) {
        create(player);
        BigDecimal exactMoney = defaultMoney();
        double money = exactMoney.doubleValue();
        new BigDecimal(money);
        money = config.getDouble("Money");
        exactMoney = BigDecimal.valueOf(money);
        return exactMoney;
    }

    public void setMoney(Player player, BigDecimal money) {
        create(player);
        set("Money", money);
        save();
    }

    public void addMoney(Player player, BigDecimal money) {
        create(player);
        BigDecimal amount = getMoney(player).add(money, MathContext.DECIMAL128);
        set("Money", amount);
        save();
    }

    public void removeMoney(Player player, BigDecimal money) {
        create(player);
        BigDecimal amount = getMoney(player).subtract(money, MathContext.DECIMAL128);
        set("Money", amount);
        save();
    }

    public void resetMoney(Player player) {
        create(player);
        set("Money", defaultMoney());
        save();
    }

    public void payMoney(Player player, Player target, BigDecimal money) {
        removeMoney(player, money);
        addMoney(target, money);
    }

    public boolean hasEnoughMoney(Player player, double money) {
        create(player);
        return money <= getMoney(player).doubleValue();
    }

    public BigDecimal defaultMoney() {
        return BigDecimal.valueOf(defaultConfig.getDouble(Configs.ECONOMY_DEFAULT_MONEY));
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }
}
