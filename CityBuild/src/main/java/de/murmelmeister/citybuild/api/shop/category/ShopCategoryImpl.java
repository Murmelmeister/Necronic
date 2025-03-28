package de.murmelmeister.citybuild.api.shop.category;

import de.murmelmeister.citybuild.CityBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ShopCategoryImpl implements ShopCategory {
    private final UUID id;
    private String displayName;
    private Material icon;
    private String description;

    private String[] lore;

    public ShopCategoryImpl(UUID id, String displayName, String icon) {
        this(id, displayName, icon, null);
    }

    public ShopCategoryImpl(UUID id, String displayName, String icon, String description) {
        this.id = id;
        this.displayName = displayName;
        this.icon = Material.getMaterial(icon);
        this.description = description;
        this.lore = description != null ? description.split("\n") : null;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Material getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Material icon) {
        this.icon = icon;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String[] getLore() {
        return lore;
    }

    @Override
    public void setLore(String[] lore) {
        this.lore = lore;
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        // container.set(CityBuild.getKeyShopCategory(), PersistentDataType.STRING, id.toString());
        container.set(CityBuild.getKeyShopCategory(), PersistentDataType.BOOLEAN, true);
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(displayName));

        List<Component> lores = new ArrayList<>();
        if (lore != null)
            for (String lore : lore)
                lores.add(MiniMessage.miniMessage().deserialize(lore));
        itemMeta.lore(lores);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
