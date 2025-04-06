package de.murmelmeister.citybuild.api.shop.category;

import de.murmelmeister.citybuild.files.ConfigFile;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public sealed interface ShopCategory permits ShopCategoryProvider {
    /**
     * Checks if a shop category with the given UUID exists in the database.
     *
     * @param id         The UUID of the category to check
     * @return True if the category exists, false otherwise
     */
    boolean existsCategory(UUID id);

    /**
     * Checks if a shop category with the given internal name exists in the database.
     *
     * @param internName The internal name of the category to check
     * @return True if the category exists, false otherwise
     */
    boolean existsCategory(String internName);

    /**
     * Adds a new shop category with the specified display name and icon.
     *
     * @param internName  The internal name of the category
     * @param displayName The display name of the category
     * @param icon        The material used as the icon for the category
     */
    void addCategory(String internName, String displayName, Material icon);

    /**
     * Adds a new shop category with the specified display name, icon, and description.
     *
     * @param internName  The internal name of the category
     * @param displayName The display name of the category.
     * @param icon        The material icon representing the category.
     * @param description A description of the category, optional (can be null).
     */
    void addCategory(String internName, String displayName, Material icon, String description);

    /**
     * Adds a new shop category to the system using the specified unique identifier, display name, and icon.
     *
     * @param id          The unique identifier for the shop category.
     * @param internName  The internal name of the category
     * @param displayName The display name of the shop category.
     * @param icon        The material icon representing the shop category.
     */
    void addCategory(UUID id, String internName, String displayName, Material icon);

    /**
     * Adds a new shop category to the system with a unique identifier.
     *
     * @param id          The unique identifier for the category.
     * @param internName  The internal name of the category
     * @param displayName The display name of the category.
     * @param icon        The icon representing the category, based on {@link Material}.
     * @param description A textual description for the category, providing additional details.
     */
    void addCategory(UUID id, String internName, String displayName, Material icon, String description);

    /**
     * Removes a shop category associated with the specified unique identifier.
     *
     * @param id The unique identifier of the category to be removed
     */
    void removeCategory(UUID id);

    /**
     * Updates the display name of a shop category.
     *
     * @param id          The unique identifier of the category
     * @param displayName The new display name for the category
     */
    void updateDisplayName(UUID id, String displayName);

    /**
     * Updates the icon of a shop category.
     *
     * @param id   The unique identifier of the category
     * @param icon The new icon material for the category
     */
    void updateIcon(UUID id, Material icon);

    /**
     * Updates the internal name of a shop category.
     *
     * @param id         The unique identifier of the category
     * @param internName The new internal name for the category
     */
    void updateDescription(UUID id, String description);

    /**
     * Retrieves a list of UUIDs representing all available categories.
     *
     * @return A list of UUIDs for all categories
     */
    List<UUID> getCategories();

    /**
     * Retrieves a list of internal names for all available categories.
     *
     * @return A list of internal names for all categories
     */
    List<String> getInternNames();

    /**
     * Retrieves the unique identifier of a category based on its internal name.
     *
     * @param internName The internal name of the category
     * @return The UUID of the category, or null if not found
     */
    UUID getCategoryId(String internName);

    /**
     * Retrieves the internal name associated with the specified category ID.
     *
     * @param id The unique identifier of the category
     * @return The internal name of the specified category, or null if not found
     */
    String getInternName(UUID id);

    /**
     * Retrieves the display name associated with the specified category ID.
     *
     * @param id The unique identifier of the category
     * @return The display name of the specified category, or null if the category does not exist
     */
    String getDisplayName(UUID id);

    /**
     * Retrieves the icon material associated with the specified category ID.
     *
     * @param id The unique identifier of the category whose icon is to be retrieved
     * @return The Material representing the icon of the category, or null if no icon is found
     */
    Material getIcon(UUID id);

    /**
     * Retrieves the description associated with the specified category ID.
     *
     * @param id The unique identifier of the category for which the description is to be retrieved.
     * @return The description of the category as a string, or null if no description is found.
     */
    String getDescription(UUID id);

    /**
     * Retrieves the lore associated with the specified category ID.
     *
     * @param id The unique identifier for the category
     * @return An array of strings representing the lore lines, or null if no lore is available
     */
    String[] getLore(UUID id);

    /**
     * Retrieves an {@link ItemStack} representing the shop category associated with the specified UUID.
     * The ItemStack is configured with the display name, lore, and icon provided for the shop category.
     *
     * @param id The UUID of the shop category to retrieve the ItemStack for.
     * @return The {@link ItemStack} representing the shop category, or a default value if the category does not exist.
     */
    ItemStack getItemStack(UUID id);

    /**
     * Imports categories from a CSV file into the shop system.
     *
     * @param logger A Logger instance to log messages and errors during the import process
     * @param config A ConfigFile instance containing configurations needed for the import
     */
    void importCSV(Logger logger, ConfigFile config);
}
