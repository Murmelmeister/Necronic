package de.murmelmeister.citybuild.api.home;

import org.bukkit.Location;
import org.bukkit.Server;

public final class HomeImpl implements Home {
    private final HomeKeys keys;
    private String worldName;
    private String worldType;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public HomeImpl(int userId, String name, Location location) {
        this(new HomeKeys(userId, name), location);
    }

    public HomeImpl(HomeKeys keys, Location location) {
        this(keys,
                location.getWorld().getName(),
                location.getWorld().getEnvironment().name(),
                location.getBlockX() + 0.5D,
                location.getBlockY() + 0.25D,
                location.getBlockZ() + 0.5D,
                Math.round(location.getYaw() / 45.0F) * 45,
                Math.round(location.getPitch() / 45.0F) * 45);
    }

    public HomeImpl(HomeKeys keys, String worldName, String worldType, double x, double y, double z, double yaw, double pitch) {
        this.keys = keys;
        this.worldName = worldName;
        this.worldType = worldType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public HomeKeys getKeys() {
        return keys;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    @Override
    public String getWorldType() {
        return worldType;
    }

    @Override
    public void setWorldType(String worldType) {
        this.worldType = worldType;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public double getYaw() {
        return yaw;
    }

    @Override
    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    @Override
    public double getPitch() {
        return pitch;
    }

    @Override
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    @Override
    public Location getLocation(Server server) {
        return new Location(server.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);
    }
}
