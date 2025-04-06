package de.murmelmeister.citybuild.api.home;

import org.bukkit.Location;
import org.bukkit.Server;

import java.util.List;

public sealed interface Home permits HomeProvider {
    /**
     * Checks if a home with the specified name exists for the given user.
     *
     * @param userId   The ID of the user.
     * @param homeName The name of the home to check.
     * @return true if the home exists, false otherwise.
     */
    boolean existsHome(int userId, String homeName);

    /**
     * Adds a home for the specified user with the given name and location.
     *
     * @param userId    The ID of the user.
     * @param homeName  The name of the home to add.
     * @param location  The location of the home.
     */
    void addHome(int userId, String homeName, Location location);

    /**
     * Removes a home for the specified user with the given name.
     *
     * @param userId   The ID of the user.
     * @param homeName The name of the home to remove.
     */
    void removeHome(int userId, String homeName);

    /**
     * Retrieves the location of a home for the specified user with the given name.
     *
     * @param server    The server instance.
     * @param userId    The ID of the user.
     * @param homeName  The name of the home to retrieve.
     * @return The location of the home, or null if it does not exist.
     */
    Location getHome(Server server, int userId, String homeName);

    /**
     * Retrieves a list of home names for the specified user.
     *
     * @param userId The ID of the user.
     * @return A list of home names associated with the user.
     */
    List<String> getHomeNames(int userId);
}
