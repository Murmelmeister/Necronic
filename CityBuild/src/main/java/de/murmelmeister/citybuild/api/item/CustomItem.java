package de.murmelmeister.citybuild.api.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public sealed interface CustomItem permits CustomItemProvider {
    /**
     * Checks whether an item with the specified unique identifier exists.
     *
     * @param id The unique identifier of the item to check
     * @return True if the item exists, false otherwise
     */
    boolean existsItem(UUID id);

    /**
     * Adds a custom item with the specified display name, icon name, and material.
     *
     * @param displayName The display name of the custom item
     * @param iconName    The icon name of the custom item
     * @param material    The material representing the custom item
     */
    void addItem(String displayName, String iconName, Material material);

    /**
     * Adds a new custom item with the specified display name, icon name, material, and description.
     *
     * @param displayName The display name of the item. It represents the visual name the user sees.
     * @param iconName    The icon name of the item. It specifies the visual representation or text component for the icon.
     * @param material    The material type of the item. It determines the type of the item in terms of Minecraft material.
     * @param description The description of the item. It provides additional details or lore about the item.
     */
    void addItem(String displayName, String iconName, Material material, String description);

    /**
     * Adds a custom item with the specified ID, display name, icon name, and material.
     *
     * @param id          The unique identifier for the custom item.
     * @param displayName The display name of the custom item.
     * @param iconName    The icon name representing the custom item visually.
     * @param material    The material type of the custom item.
     */
    void addItem(UUID id, String displayName, String iconName, Material material);

    /**
     * Adds a new custom item with the specified properties to the system.
     *
     * @param id          The unique identifier for the custom item.
     * @param displayName The display name of the custom item.
     * @param iconName    The icon name representing the custom item.
     * @param material    The {@link Material} type of the custom item.
     * @param description Optional description of the custom item.
     */
    void addItem(UUID id, String displayName, String iconName, Material material, String description);

    /**
     * Removes a custom item identified by the given unique identifier.
     *
     * @param id The UUID of the custom item to be removed
     */
    void removeItem(UUID id);

    /**
     * Updates an existing custom item in the database with the specified parameters.
     *
     * @param id          The unique identifier of the custom item to be updated.
     * @param displayName The new display name of the custom item.
     * @param iconName    The new icon name of the custom item, representing its visual name.
     * @param material    The new material type of the custom item.
     * @param description The new description of the custom item, providing additional details.
     */
    void updateItem(UUID id, String displayName, String iconName, Material material, String description);

    /**
     * Retrieves a list of unique identifiers for all custom items.
     *
     * @return A list of UUIDs representing the IDs of all custom items.
     */
    List<UUID> getCustomItemIds();

    /**
     * Retrieves a list of internal names for all custom items.
     *
     * @return A list of strings representing the internal names of all custom items.
     */
    List<String> getInternNames();

    /**
     * Retrieves the unique identifier of a custom item based on its internal name.
     *
     * @param internName The internal name of the custom item
     * @return The UUID of the custom item, or null if no item exists with the given internal name
     */
    UUID getCustomItemId(String internName);

    /**
     * Retrieves the internal name of a custom item associated with the given UUID.
     *
     * @param id The unique identifier of the custom item
     * @return The internal name of the custom item, or null if no item is found with the given UUID
     */
    String getInternName(UUID id);

    /**
     * Retrieves the display name of a custom item associated with the given UUID.
     *
     * @param id The unique identifier of the custom item
     * @return The display name of the custom item, or null if no item is found with the given UUID
     */
    Component getDisplayName(UUID id);

    /**
     * Retrieves the Material associated with the custom item identified by the given UUID.
     *
     * @param id The unique identifier of the custom item
     * @return The Material corresponding to the specified item, or null if no item is found with the given UUID
     */
    Material getMaterial(UUID id);

    /**
     * Retrieves the description associated with a specific custom item identified by its UUID.
     *
     * @param id The unique identifier of the custom item
     * @return The description of the custom item, or null if no description is found
     */
    String getDescription(UUID id);

    /**
     * Retrieves the lore of a custom item associated with the given UUID.
     * The lore is derived from the item's description, with each line
     * of the description representing an individual element in the returned array.
     *
     * @param id The unique identifier of the custom item
     * @return An array of strings representing the lore of the item,
     * or null if no description is found for the specified item
     */
    String[] getLore(UUID id);

    /**
     * Constructs and returns the {@link ItemStack} associated with the given UUID.
     * The ItemStack's metadata such as display name, icon, material, and lore
     * is determined and set based on the data associated with the UUID.
     *
     * @param id The unique identifier of the custom item
     * @return The {@link ItemStack} corresponding to the unique identifier
     */
    ItemStack getItemStack(UUID id);

    /**
     * Adds default materials to the custom items collection. This method iterates over all available
     * materials in the application, except those categorized as "air" or non-item materials, and adds
     * them as custom items if they do not already exist.
     * <p>
     * Each eligible material is assigned a new unique identifier (UUID) and is stored with its display
     * name and icon name derived from the material's internationalized display name. If a material
     * already exists in the collection, it is skipped.
     */
    void addDefaultMaterials();
}
