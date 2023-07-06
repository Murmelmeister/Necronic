package de.murmelmeister.citybuild.command.commands.inventories;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderChestCommand extends CommandManager {
    public EnderChestCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ENDER_CHEST_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ENDER_CHEST_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        if (args.length == 0) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_ENDER_CHEST_USE))) {
                sendMessage(player, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(player.hasPermission(config.getString(Configs.PERMISSION_ENDER_CHEST_USE)))) {
                sendMessage(player, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Inventory enderChest = player.getEnderChest();
            player.openInventory(enderChest);

        } else if (args.length == 1) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_ENDER_CHEST_OTHER))) {
                sendMessage(player, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(player.hasPermission(config.getString(Configs.PERMISSION_ENDER_CHEST_OTHER)))) {
                sendMessage(player, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player target = player.getServer().getPlayer(args[0]);

            if (target == null) {
                sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            Inventory enderChest = target.getEnderChest();
            player.openInventory(enderChest);

        } else {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return playerTabComplete(sender, args);
    }
}
