package de.murmelmeister.citybuild.api.item;

import org.bukkit.Material;

import java.util.UUID;

public sealed interface CustomItem permits CustomItemImpl {
    UUID getId();

    String getDisplayName();

    void setDisplayName(String displayName);

    Material getMaterial();

    void setMaterial(Material material);

    String getDescription();

    void setDescription(String description);

    String[] getLore();

    void setLore(String[] lore);
}
