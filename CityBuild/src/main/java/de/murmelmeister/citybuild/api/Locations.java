package de.murmelmeister.citybuild.api;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.util.ConfigUtil;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Locations {
    private final Logger logger;
    private final Server server;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;
    private List<String> locationList;

    public Locations(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.server = main.getInstance().getServer();
        this.defaultConfig = main.getConfig();
    }

    public void create() {
        String fileName = "locations.yml";
        this.file = new File(String.format("plugins//%s//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
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

    /**
     * Set yourself a new location, but it can be overwritten.
     *
     * @param location The location where it is saved
     * @param name     The name of the location
     */
    public void setLocation(Location location, String name) {
        String path = "Locations." + name;
        String worldName = location.getWorld().getName();
        String environment = location.getWorld().getEnvironment().name();
        double x = location.getBlockX() + 0.5D;
        double y = location.getBlockY() + 0.25D;
        double z = location.getBlockZ() + 0.5D;
        double yaw = Math.round(location.getYaw() / 45.0F) * 45;
        double pitch = Math.round(location.getPitch() / 45.0F) * 45;

        create();
        this.locationList = getLocationList();
        if (!(locationList.contains(name))) {
            locationList.add(name);
            set("LocationList", locationList);
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

    public void setSpawn(Location location) {
        setLocation(location, "Spawn");
    }

    /**
     * Deletes the location from the config.
     *
     * @param name The name of the location
     */
    public void removeLocation(String name) {
        create();
        this.locationList = getLocationList();
        locationList.remove(name);
        set("LocationList", locationList);
        set("Locations." + name, null);
        save();
    }

    public void removeSpawn() {
        removeLocation("Spawn");
    }

    /**
     * Gives the location.
     *
     * @param name The name of the location
     * @return The location of the name
     */
    public Location getLocation(String name) {
        create();
        String path = "Locations." + name;
        String worldName = getString(path + ".WorldName");
        double x = getDouble(path + ".X");
        double y = getDouble(path + ".Y");
        double z = getDouble(path + ".Z");
        double yaw = getDouble(path + ".Yaw");
        double pitch = getDouble(path + ".Pitch");

        World world = server.getWorld(worldName);
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public Location getSpawn() {
        return getLocation("Spawn");
    }

    /**
     * Whether the location exists.
     *
     * @param name The name of the location
     * @return If the name exists in the config
     */
    public boolean isLocationExist(String name) {
        create();
        return config.get("Locations." + name) != null;
    }

    public boolean isSpawnExist() {
        return isLocationExist("Spawn");
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

    public List<String> getLocationList() {
        this.locationList = new ArrayList<>();
        if (config.contains("LocationList")) locationList = config.getStringList("LocationList");
        return locationList;
    }
}
