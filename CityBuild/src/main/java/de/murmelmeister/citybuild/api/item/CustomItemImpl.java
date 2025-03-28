package de.murmelmeister.citybuild.api.item;

import org.bukkit.Material;

import java.util.UUID;

public final class CustomItemImpl implements CustomItem {
    private final UUID id;
    private String displayName;
    private Material material;
    private String description;

    private String[] lore;

    public CustomItemImpl(UUID id, String displayName, String material) {
        this(id, displayName, material, null);
    }

    public CustomItemImpl(UUID id, String displayName, String material, String description) {
        this.id = id;
        this.displayName = displayName;
        this.material = Material.getMaterial(material);
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
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
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
}
