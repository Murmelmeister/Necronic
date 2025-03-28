package de.murmelmeister.citybuild.api.shop.item;

import de.murmelmeister.citybuild.files.ConfigFile;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public sealed interface ShopItemProvider permits ShopItemProviderImpl {
    ShopItem getItem(UUID id);

    boolean containsItem(UUID id);

    void addItem(ShopItem item);

    void removeItem(UUID id);

    void updateItem(ShopItem item);

    List<UUID> getCategoryItems(UUID category);

    void importCSV(Logger logger, ConfigFile config);
}
