package de.murmelmeister.citybuild.api.shop.item;

import de.murmelmeister.citybuild.api.economy.Economy;
import de.murmelmeister.citybuild.api.item.CustomItem;
import de.murmelmeister.citybuild.api.shop.category.ShopCategory;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.files.MessageFile;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public sealed interface ShopItem permits ShopItemProvider {
    /**
     * Checks whether an item with the specified ID exists.
     *
     * @param id The unique identifier of the item to check
     * @return True if the item exists, false otherwise
     */
    boolean existsItem(UUID id);

    /**
     * Adds a new shop item with the specified parameters to the shop system.
     *
     * @param customItemId The unique identifier for the custom item
     * @param categoryId   The unique identifier for the category to which the item belongs
     * @param icon         The material representing the icon of the shop item
     * @param buyPrice     The price at which the item can be purchased
     * @param sellPrice    The price at which the item can be sold
     */
    void addItem(UUID customItemId, UUID categoryId, Material icon, double buyPrice, double sellPrice);

    /**
     * Adds a new item to the shop with the specified details.
     *
     * @param id           The unique identifier of the item.
     * @param customItemId The unique identifier of the associated custom item.
     * @param categoryId   The unique identifier of the category to which this item belongs.
     * @param icon         The material representing the item's icon.
     * @param buyPrice     The price at which the item can be bought from the shop.
     * @param sellPrice    The price at which the item can be sold to the shop.
     */
    void addItem(UUID id, UUID customItemId, UUID categoryId, Material icon, double buyPrice, double sellPrice);

    /**
     * Removes an item identified by its unique ID from the shop.
     *
     * @param id The unique identifier of the item to be removed
     */
    void removeItem(UUID id);

    /**
     * Updates the icon of an item identified by its unique ID.
     *
     * @param id   The unique identifier of the item to be updated
     * @param icon The new icon material to be set for the item
     */
    void updateIcon(UUID id, Material icon);

    /**
     * Updates the buy price of an item identified by its unique ID.
     *
     * @param id       The unique identifier of the item to be updated
     * @param buyPrice The new buy price to be set for the item
     */
    void updateBuyPrice(UUID id, double buyPrice);

    /**
     * Updates the sell price of an item identified by its unique ID.
     *
     * @param id        The unique identifier of the item to be updated
     * @param sellPrice The new sell price to be set for the item
     */
    void updateSellPrice(UUID id, double sellPrice);

    /**
     * Retrieves a list of item IDs belonging to the specified category.
     *
     * @param categoryId The UUID of the category whose items are to be retrieved
     * @return A list of UUIDs representing the items in the specified category
     */
    List<UUID> getCategoryItems(UUID categoryId);

    /**
     * Retrieves the unique identifier of a shop item based on its custom item ID and category ID.
     *
     * @param customItemId The unique identifier of the custom item
     * @param categoryId   The unique identifier of the category
     * @return The unique identifier of the shop item if it exists, or null if no such item is found
     */
    UUID getItemId(UUID customItemId, UUID categoryId);

    /**
     * Retrieves the unique identifier of a custom item associated with the specified ID.
     *
     * @param id The unique identifier used to look up the custom item
     * @return The unique identifier of the custom item if it exists, or null if no custom item is associated with the provided ID
     */
    UUID getCustomItem(UUID id);

    /**
     * Retrieves the category ID associated with the given item ID.
     *
     * @param id The unique identifier of the item whose category is to be retrieved
     * @return The unique identifier of the category associated with the specified item
     */
    UUID getCategory(UUID id);

    /**
     * Retrieves the icon material of a shop item identified by the provided unique identifier.
     *
     * @param id The unique identifier of the shop item whose icon is to be retrieved
     * @return The Material representing the icon of the specified shop item
     */
    Material getIcon(UUID id);

    /**
     * Retrieves the buy price of the item identified by the given unique identifier (UUID).
     *
     * @param id The unique identifier of the item for which the buy price is to be retrieved
     * @return The buy price of the specified item
     */
    double getBuyPrice(UUID id);

    /**
     * Retrieves the sell price of an item identified by its unique identifier.
     *
     * @param id The unique identifier of the item whose sell price is to be retrieved
     * @return The sell price of the specified item
     */
    double getSellPrice(UUID id);

    /**
     * Retrieves an ItemStack representation based on the provided parameters such as configuration, messages,
     * custom item configurations, economy data, and user context.
     *
     * @param config     An instance of ConfigFile containing configuration values needed to construct the ItemStack
     * @param message    An instance of MessageFile containing message configurations for use with the ItemStack
     * @param customItem An instance of CustomItem representing custom item definitions necessary for the ItemStack
     * @param economy    An instance of Economy to handle economy interactions, such as cost or balance checks
     * @param userId     The ID of the user for whom the ItemStack is being retrieved
     * @param id         The unique identifier for the shop item being fetched
     * @return An ItemStack instance constructed based on the provided parameters
     */
    ItemStack getItemStack(ConfigFile config, MessageFile message, CustomItem customItem, Economy economy, int userId, UUID id);

    /**
     * Imports a CSV file and processes it based on the provided configuration, custom item, and category.
     *
     * @param logger     The logger used to log messages during the import process
     * @param config     The configuration file containing settings required for the import
     * @param customItem The custom item associated with the data being imported
     * @param category   The shop category where the imported items belong
     */
    void importCSV(Logger logger, ConfigFile config, CustomItem customItem, ShopCategory category);
}
