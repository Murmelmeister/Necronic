package de.murmelmeister.citybuild.api.home;

import org.bukkit.Location;
import org.bukkit.Server;

public sealed interface Home permits HomeImpl {
    HomeKeys getKeys();

    String getWorldName();

    void setWorldName(String worldName);

    String getWorldType();

    void setWorldType(String worldType);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    double getYaw();

    void setYaw(double yaw);

    double getPitch();

    void setPitch(double pitch);

    Location getLocation(Server server);
}
