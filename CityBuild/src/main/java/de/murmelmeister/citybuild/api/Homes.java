package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.util.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Homes {
    private final Logger logger;

    private File file;
    private YamlConfiguration config;
    private List<String> homeList;

    public Homes(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
    }

    public void create(Player player) {
        UUID uuid = player.getUniqueId();
        String fileName = uuid + ".yml";
        this.file = new File("plugins//NetherLegends//Homes//", fileName);
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

    public void createUsername(Player player) {
        create(player);
        set("Username", player.getName());
        this.homeList = getHomeList();
        set("HomeList", homeList);
        save();
    }

    public void addHome(Player player, String name) {
        Location location = player.getLocation();
        String path = "Homes." + name;

        String worldName = location.getWorld().getName();
        String environment = location.getWorld().getEnvironment().name();
        double x = location.getBlockX() + 0.5D;
        double y = location.getBlockY() + 0.25D;
        double z = location.getBlockZ() + 0.5D;
        double yaw = Math.round(location.getYaw() / 45.0F) * 45;
        double pitch = Math.round(location.getPitch() / 45.0F) * 45;

        create(player);
        this.homeList = getHomeList();
        if (!(homeList.contains(name))) {
            homeList.add(name);
            set("HomeList", homeList);
        }

        set(path + ".Name", name);
        set(path + ".WorldName", worldName);
        set(path + ".Environment", environment);
        set(path + ".X", x);
        set(path + ".Y", y);
        set(path + ".Z", z);
        set(path + ".Yaw", yaw);
        set(path + ".Pitch", pitch);
        save();
    }

    public void removeHome(Player player, String name) {
        create(player);
        this.homeList = getHomeList();
        homeList.remove(name);
        set("HomeList", homeList);
        set("Homes." + name, null);
        save();
    }

    public Location getHome(Player player, String name) {
        create(player);
        String path = "Homes." + name;
        String worldName = getString(path + ".WorldName");
        double x = getDouble(path + ".X");
        double y = getDouble(path + ".Y");
        double z = getDouble(path + ".Z");
        double yaw = getDouble(path + ".Yaw");
        double pitch = getDouble(path + ".Pitch");

        World world = player.getServer().getWorld(worldName);
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public boolean hasHome(Player player, String name) {
        create(player);
        return config.get("Homes." + name) != null;
    }

    public List<String> getHomeList() {
        this.homeList = new ArrayList<>();
        if (config.contains("HomeList")) homeList = config.getStringList("HomeList");
        return homeList;
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }

    private String getString(String path) {
        return config.getString(path);
    }

    private double getDouble(String path) {
        return config.getDouble(path);
    }
}
