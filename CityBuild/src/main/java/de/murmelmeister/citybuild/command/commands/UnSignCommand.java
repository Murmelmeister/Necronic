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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_UN_SIGN))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_UN_SIGN)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        if (player.getItemInHand().getType().equals(Material.AIR)) return true;

        if (player.hasPermission(config.getString(Configs.PERMISSION_NOT_COOLDOWN))) {
            createItem(player.getItemInHand());
            return true;
        }

        if (cooldown.getDuration(player, "UnSign") <= System.currentTimeMillis())
            cooldown.removeCooldown(player, "UnSign");

        if (cooldown.hasCooldown(player, "UnSign")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player, "UnSign").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return true;
        }

        cooldown.addCooldown(player, "Sign", config.getLong(Configs.TIME_UN_SIGN_COOLDOWN));
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
