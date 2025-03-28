package de.murmelmeister.citybuild.api.shop.item;

import de.murmelmeister.citybuild.api.economy.EconomyProvider;
import de.murmelmeister.citybuild.api.item.CustomItemProvider;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.files.MessageFile;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public sealed interface ShopItem permits ShopItemImpl {
    UUID getId();

    UUID getCustomItem();

    void setCustomItem(UUID customItem);

    UUID getCategory();

    void setCategory(UUID category);

    Material getIcon();

    void setIcon(Material icon);

    double getBuyPrice();

    void setBuyPrice(double buyPrice);

    double getSellPrice();

    void setSellPrice(double sellPrice);

    ItemStack toItemStack(ConfigFile config, MessageFile message, CustomItemProvider customItem, EconomyProvider economy, int userId);
}
