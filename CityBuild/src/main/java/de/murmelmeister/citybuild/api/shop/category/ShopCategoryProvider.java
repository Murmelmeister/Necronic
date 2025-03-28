package de.murmelmeister.citybuild.api.shop.category;

import de.murmelmeister.citybuild.files.ConfigFile;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public sealed interface ShopCategoryProvider permits ShopCategoryProviderImpl {
    ShopCategory getCategory(UUID id);

    boolean containsCategory(UUID id);

    void addCategory(ShopCategory category);

    void removeCategory(UUID id);

    void updateCategory(ShopCategory category);

    List<UUID> getCategories();

    void importCSV(Logger logger, ConfigFile config);
}
