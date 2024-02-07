package de.murmelmeister.citybuild.command.commands.inventories;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.OfflinePlayer;
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

    /*
    /enderChest [player]
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ENDER_CHEST_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_ENDER_CHEST_COMMAND))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length == 0) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_ENDER_CHEST_USE))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_ENDER_CHEST_USE))) return true;

            enderChest.createEnderChestGUI(player);

            /*int size = this.enderChest.getInventorySize(player.getUniqueId(), 1) == null ? 3 * 9 : Integer.parseInt(this.enderChest.getInventorySize(player.getUniqueId(), 1));
            Inventory enderChest = this.enderChest.loadInventory(player, 1, HexColor.format("&dEnderChest - " + 1), size);
            this.enderChest.setInventory(enderChest);
            player.openInventory(this.enderChest.getInventory());*/

        } else if (args.length == 1) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_ENDER_CHEST_OTHER))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_ENDER_CHEST_OTHER))) return true;

            Player target = player.getServer().getPlayer(args[0]);
            if (target == null) {
                OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[0]);
                if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                    enderChest.createEnderChestGUI(player, offlinePlayer);
                } else sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            // TODO: Add other players
            enderChest.createEnderChestGUI(player, target);
            /*Inventory enderChest = target.getEnderChest();
            player.openInventory(enderChest);*/

        } else sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabCompletePlayers(sender, args, 1);
    }
}
