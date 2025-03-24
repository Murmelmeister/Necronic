package de.murmelmeister.citybuild.api.shop;

import de.murmelmeister.citybuild.api.economy.EconomyProvider;
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

public final class ShopMenu extends MultipleMenu<String> {
    private final User user;
    private final ConfigFile configFile;
    private final MessageFile messageFile;
    private final ShopCategory category;
    private final ShopItem item;
    private final EconomyProvider economy;

    public ShopMenu(User user, ConfigFile configFile, MessageFile messageFile, ShopCategory category, ShopItem item, EconomyProvider economy) {
        super(null, true, category.getCategories());
        this.user = user;
        this.configFile = configFile;
        this.messageFile = messageFile;
        this.category = category;
        this.item = item;
        this.economy = economy;
        setTitle(configFile.getString(Configs.SHOP_CATEGORY_TITLE));
        Material material = Material.getMaterial(configFile.getString(Configs.SHOP_CATEGORY_PLACEHOLDER));
        if (material != null) setPlaceholder(material);
    }

    @Override
    protected ItemStack convertToItemStack(String categoryId) {
        return category.getIcon(categoryId);
    }

    @Override
    protected void handlePageClick(Player player, String categoryId, ClickType clickType) {
        if (item.getCategoryItems(categoryId).isEmpty()) {
            player.closeInventory();
            sendMessage(player, messageFile.getString(Messages.SHOP_CATEGORY_ITEMS_DOES_NOT_EXIST));
            return;
        }
        new CategoryItemMenu(this, user, configFile, messageFile, category, item, economy, categoryId).show(player);
    }

    public void sendMessage(CommandSender sender, String message) {
        if (configFile.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(MiniMessage.miniMessage().deserialize(this.messageFile.prefix() + message));
        else sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private static class CategoryItemMenu extends MultipleMenu<String> {
        private final User user;
        private final ConfigFile configFile;
        private final MessageFile messageFile;
        private final ShopItem shopItem;
        private final EconomyProvider economy;

        public CategoryItemMenu(Menu parent, User user, ConfigFile configFile, MessageFile messageFile, ShopCategory category, ShopItem shopItem, EconomyProvider economy, String categoryId) {
            super(parent, false, shopItem.getCategoryItems(categoryId));
            this.user = user;
            this.configFile = configFile;
            this.messageFile = messageFile;
            this.shopItem = shopItem;
            this.economy = economy;
            setTitle(configFile.getString(Configs.SHOP_ITEM_TITLE).replace("[CATEGORY]", category.getDisplayName(categoryId)));
            setPlaceholder(Material.getMaterial(configFile.getString(Configs.SHOP_ITEM_PLACEHOLDER)));
        }

        @Override
        protected ItemStack convertToItemStack(String itemId) {
            int userId = user.getId(getViewer().getUniqueId());
            return shopItem.getIcon(configFile, messageFile, economy, userId, itemId);
        }

        @Override
        protected void handlePageClick(Player player, String itemId, ClickType clickType) {
            Material material = shopItem.getMaterial(itemId);
            if (material == null) return;
            int maxStackSize = material.getMaxStackSize();
            int userId = user.getId(player.getUniqueId());
            double buy = shopItem.getBuyPrice(itemId);
            Component noMoneyMessage = MiniMessage.miniMessage().deserialize(messageFile.getString(Messages.SHOP_ITEM_MONEY_NOT_ENOUGH));
            if (clickType.isLeftClick()) {
                if (!economy.hasEnoughMoney(userId, buy)) {
                    player.closeInventory();
                    player.sendMessage(noMoneyMessage);
                    return;
                }

                economy.removeMoney(userId, buy);
                player.getInventory().addItem(shopItem.getItemStack(material, 1));
            }
            if (maxStackSize != 1)
                if (clickType.isRightClick()) {
                    if (!economy.hasEnoughMoney(userId, buy * maxStackSize)) {
                        player.closeInventory();
                        player.sendMessage(noMoneyMessage);
                        return;
                    }

                    economy.removeMoney(userId, buy * maxStackSize);
                    player.getInventory().addItem(shopItem.getItemStack(material, maxStackSize));
                }
        }
    }
}
