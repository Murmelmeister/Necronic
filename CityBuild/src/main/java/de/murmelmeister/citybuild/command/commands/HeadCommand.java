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
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HeadCommand extends CommandManager {
    public HeadCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_HEAD))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_HEAD)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        if (args.length == 0) {
            setFunction(player, player.getName());
        } else if (args.length == 1) {
            setFunction(player, args[0]);
        } else {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private void setFunction(Player player, String name) {
        if (player.hasPermission(config.getString(Configs.PERMISSION_NOT_COOLDOWN))) {
            player.getInventory().addItem(addHead(name));
            return;
        }

        if (cooldown.getDuration(player, "Head") <= System.currentTimeMillis())
            cooldown.removeCooldown(player, "Head");

        if (cooldown.hasCooldown(player, "Head")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player, "Head").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return;
        }

        cooldown.addCooldown(player, "Head", config.getLong(Configs.TIME_HEAD_COOLDOWN));
        player.getInventory().addItem(addHead(name));
    }

    private ItemStack addHead(String name) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(name);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
}
