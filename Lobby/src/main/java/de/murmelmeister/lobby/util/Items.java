package de.murmelmeister.lobby.util;

import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Items {
    public static void setLobbyItems(Config config, Player player) {
        player.getInventory().clear();
        player.getOpenInventory().setItem(config.getInt(Configs.LOBBY_ITEMS_NAVIGATOR_SLOT), createNavigator(config));
    }

    private static ItemStack createNavigator(Config config) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(config.getString(Configs.LOBBY_ITEMS_NAVIGATOR_MATERIAL))));
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(HexColor.format(config.getString(Configs.LOBBY_ITEMS_NAVIGATOR_DISPLAY_NAME)));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
