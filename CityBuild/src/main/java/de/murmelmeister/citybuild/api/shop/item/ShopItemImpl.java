package de.murmelmeister.citybuild.api.shop.item;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.economy.EconomyProvider;
import de.murmelmeister.citybuild.api.item.CustomItem;
import de.murmelmeister.citybuild.api.item.CustomItemProvider;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.files.MessageFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ShopItemImpl implements ShopItem {
    private final UUID id;
    private UUID customItem;
    private UUID category;
    private Material icon;
    private double buyPrice;
    private double sellPrice;

    public ShopItemImpl(UUID id, UUID customItem, UUID category, String icon, double buyPrice, double sellPrice) {
        this.id = id;
        this.customItem = customItem;
        this.category = category;
        this.icon = Material.getMaterial(icon);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getCustomItem() {
        return customItem;
    }

    @Override
    public void setCustomItem(UUID customItem) {
        this.customItem = customItem;
    }

    @Override
    public UUID getCategory() {
        return category;
    }

    @Override
    public void setCategory(UUID category) {
        this.category = category;
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
    public double getBuyPrice() {
        return buyPrice;
    }

    @Override
    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    @Override
    public double getSellPrice() {
        return sellPrice;
    }

    @Override
    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public ItemStack toItemStack(ConfigFile config, MessageFile message, CustomItemProvider customItem, EconomyProvider economy, int userId) {
        DecimalFormat format = new DecimalFormat(config.getString(Configs.PATTERN_DECIMAL));
        int maxStackSize = icon.getMaxStackSize();
        CustomItem item = customItem.getCustomItem(this.customItem);
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        //container.set(CityBuild.getKeyShopItem(), PersistentDataType.STRING, id.toString());
        container.set(CityBuild.getKeyShopItem(), PersistentDataType.BOOLEAN, true);

        itemMeta.displayName(MiniMessage.miniMessage().deserialize(item.getDisplayName()));

        List<Component> lore = new ArrayList<>();
        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_BUY_ONE)
                .replace("[PRICE]", format.format(buyPrice))));

        if (maxStackSize != 1)
            lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_BUY_STACK)
                    .replace("[PRICE]", format.format(buyPrice * maxStackSize))));

        lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_SELL)
                .replace("[SELL]", format.format(sellPrice))));

        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY)
                .replace("[MONEY]", format.format(economy.getMoney(userId)))));

        if (economy.hasEnoughMoney(userId, buyPrice))
            lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_LEFT_ITEM)
                    .replace("[MONEY]", format.format(economy.getMoney(userId) - buyPrice))));
        else
            lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH_ITEM)));

        if (maxStackSize != 1)
            if (economy.hasEnoughMoney(userId, buyPrice * maxStackSize))
                lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_LEFT_STACK)
                        .replace("[MONEY]", format.format(economy.getMoney(userId) - buyPrice * maxStackSize))));
            else
                lore.add(MiniMessage.miniMessage().deserialize(message.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH_STACK)));

        itemMeta.lore(lore);
        for (ItemFlag flag : ItemFlag.values())
            itemMeta.addItemFlags(flag);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
