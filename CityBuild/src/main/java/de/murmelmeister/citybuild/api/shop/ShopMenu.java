package de.murmelmeister.citybuild.api.shop;

import de.murmelmeister.citybuild.api.economy.EconomyProvider;
import de.murmelmeister.citybuild.api.item.CustomItemProvider;
import de.murmelmeister.citybuild.api.shop.category.ShopCategory;
import de.murmelmeister.citybuild.api.shop.category.ShopCategoryProvider;
import de.murmelmeister.citybuild.api.shop.item.ShopItem;
import de.murmelmeister.citybuild.api.shop.item.ShopItemProvider;
import de.murmelmeister.citybuild.files.ConfigFile;
import de.murmelmeister.citybuild.files.MessageFile;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import de.murmelmeister.murmelapi.menu.Menu;
import de.murmelmeister.murmelapi.menu.MultipleMenu;
import de.murmelmeister.murmelapi.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class ShopMenu extends MultipleMenu<UUID> {
    private final User user;
    private final ConfigFile configFile;
    private final MessageFile messageFile;
    private final CustomItemProvider customItems;
    private final ShopCategoryProvider shopCategory;
    private final ShopItemProvider shopItem;
    private final EconomyProvider economy;

    public ShopMenu(User user, ConfigFile configFile, MessageFile messageFile, CustomItemProvider customItems, ShopCategoryProvider category, ShopItemProvider item, EconomyProvider economy) {
        super(null, true, category.getCategories());
        this.user = user;
        this.configFile = configFile;
        this.messageFile = messageFile;
        this.customItems = customItems;
        this.shopCategory = category;
        this.shopItem = item;
        this.economy = economy;
        setTitle(configFile.getString(Configs.SHOP_CATEGORY_TITLE));
        Material material = Material.getMaterial(configFile.getString(Configs.SHOP_CATEGORY_PLACEHOLDER));
        if (material != null) setPlaceholder(material);
    }

    @Override
    protected ItemStack convertToItemStack(UUID categoryId) {
        ShopCategory category = shopCategory.getCategory(categoryId);
        return category.toItemStack();
    }

    @Override
    protected void handlePageClick(Player player, UUID categoryId, ClickType clickType) {
        if (shopItem.getCategoryItems(categoryId).isEmpty()) {
            player.closeInventory();
            sendMessage(player, messageFile.getString(Messages.SHOP_CATEGORY_ITEMS_DOES_NOT_EXIST));
            return;
        }
        new CategoryItemMenu(this, user, configFile, messageFile, customItems, shopCategory, shopItem, economy, categoryId).show(player);
    }

    public void sendMessage(CommandSender sender, String message) {
        if (configFile.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(MiniMessage.miniMessage().deserialize(this.messageFile.prefix() + message));
        else sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private static class CategoryItemMenu extends MultipleMenu<UUID> {
        private final User user;
        private final ConfigFile configFile;
        private final MessageFile messageFile;
        private final CustomItemProvider customItems;
        private final ShopItemProvider shopItem;
        private final EconomyProvider economy;

        public CategoryItemMenu(Menu parent, User user, ConfigFile configFile, MessageFile messageFile, CustomItemProvider customItems, ShopCategoryProvider category, ShopItemProvider shopItem, EconomyProvider economy, UUID categoryId) {
            super(parent, false, shopItem.getCategoryItems(categoryId));
            this.user = user;
            this.configFile = configFile;
            this.messageFile = messageFile;
            this.shopItem = shopItem;
            this.economy = economy;
            this.customItems = customItems;
            setTitle(configFile.getString(Configs.SHOP_ITEM_TITLE).replace("[CATEGORY]", category.getCategory(categoryId).getDisplayName()));
            setPlaceholder(Material.getMaterial(configFile.getString(Configs.SHOP_ITEM_PLACEHOLDER)));
        }

        @Override
        protected ItemStack convertToItemStack(UUID itemId) {
            int userId = user.getId(getViewer().getUniqueId());
            return shopItem.getItem(itemId).toItemStack(configFile, messageFile, customItems, economy, userId);
        }

        @Override
        protected void handlePageClick(Player player, UUID itemId, ClickType clickType) {
            ShopItem item = shopItem.getItem(itemId);
            if (item == null) return;
            Material material = item.getIcon();
            if (material == null) return;
            int maxStackSize = material.getMaxStackSize();
            int userId = user.getId(player.getUniqueId());
            double buy = item.getBuyPrice();
            Component noMoneyMessage = MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH));
            if (clickType.isLeftClick()) {
                if (!economy.hasEnoughMoney(userId, buy)) {
                    player.closeInventory();
                    player.sendMessage(noMoneyMessage);
                    return;
                }

                economy.removeMoney(userId, buy);
                player.getInventory().addItem(new ItemStack(material, 1));
            }
            if (maxStackSize != 1)
                if (clickType.isRightClick()) {
                    if (!economy.hasEnoughMoney(userId, buy * maxStackSize)) {
                        player.closeInventory();
                        player.sendMessage(noMoneyMessage);
                        return;
                    }

                    economy.removeMoney(userId, buy * maxStackSize);
                    player.getInventory().addItem(new ItemStack(material, maxStackSize));
                }
        }
    }
}
