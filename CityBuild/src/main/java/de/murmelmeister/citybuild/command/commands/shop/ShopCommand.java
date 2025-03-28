package de.murmelmeister.citybuild.command.commands.shop;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.shop.ShopMenu;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class ShopCommand extends CommandManager {
    public ShopCommand(CityBuild plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!isEnable(sender, Configs.COMMAND_ENABLE_SHOP_MENU)) return true;
        if (!hasPermission(sender, Configs.PERMISSION_SHOP_MENU)) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (shopCategory.getCategories().isEmpty()) {
            sendMessage(player, message.getString(Messages.SHOP_CATEGORY_EMPTY));
            return true;
        }

        ShopMenu shopMenu = new ShopMenu(user, config, message, customItems, shopCategory, shopItem, economy);
        shopMenu.show(player);
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
