package de.murmelmeister.citybuild.api.item;

import java.util.List;
import java.util.UUID;

public sealed interface CustomItemProvider permits CustomItemProviderImpl {
    CustomItem getCustomItem(UUID id);

    boolean containsCustomItem(UUID id);

    void addCustomItem(CustomItem customItem);

    void removeCustomItem(UUID id);

    void updateCustomItem(CustomItem customItem);

    List<UUID> getCustomItemIds();

    void addDefaultMaterials();
}
