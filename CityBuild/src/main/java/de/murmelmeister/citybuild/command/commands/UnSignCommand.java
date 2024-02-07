package de.murmelmeister.citybuild.command.commands;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UnSignCommand extends CommandManager {
    public UnSignCommand(Main main) {
        super(main);
    }

    /*
    /unSign
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_UN_SIGN))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_UN_SIGN))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (player.getItemInHand().getType().equals(Material.AIR)) return true;

        if (player.hasPermission(config.getString(Configs.PERMISSION_NOT_COOLDOWN))) {
            createItem(player.getItemInHand());
            return true;
        }

        if (cooldown.getDuration(player.getUniqueId(), "UnSign") <= System.currentTimeMillis())
            cooldown.removeCooldown(player.getUniqueId(), "UnSign");

        if (cooldown.hasCooldown(player.getUniqueId(), "UnSign")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player.getUniqueId(), "UnSign").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return true;
        }

        cooldown.addCooldown(player.getUniqueId(), "Sign", config.getLong(Configs.TIME_UN_SIGN_COOLDOWN));
        createItem(player.getItemInHand());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private ItemStack createItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(new ArrayList<>());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
