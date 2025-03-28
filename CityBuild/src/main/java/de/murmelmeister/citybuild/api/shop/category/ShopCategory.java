package de.murmelmeister.citybuild.api.shop.category;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public sealed interface ShopCategory permits ShopCategoryImpl {
    UUID getId();

    String getDisplayName();

    void setDisplayName(String displayName);

    Material getIcon();

    void setIcon(Material icon);

    String getDescription();

    void setDescription(String description);

    String[] getLore();

    void setLore(String[] lore);

    ItemStack toItemStack();
}
