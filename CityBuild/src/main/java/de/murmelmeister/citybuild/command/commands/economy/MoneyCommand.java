package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoneyCommand extends CommandManager {
    public MoneyCommand(Main main) {
        super(main);
    }

    /*
    /money [player]
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_MONEY_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_MONEY_COMMAND))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length == 0) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_MONEY_USE))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_MONEY_USE))) return true;
            sendMessage(player, message.getString(Messages.COMMAND_MONEY_USE).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId()))));
        } else if (args.length == 1) {
            Player target = sender.getServer().getPlayer(args[0]);

            if (target == null) {
                if (!(isEnable(sender, Configs.COMMAND_ENABLE_MONEY_OTHER))) return true;
                if (!(hasPermission(sender, Configs.PERMISSION_MONEY_OTHER))) return true;
                OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
                    sendMessage(player, message.getString(Messages.COMMAND_MONEY_OTHER).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY))
                            .replace("[MONEY]", decimalFormat.format(economy.getMoney(offlinePlayer.getUniqueId()))).replace("[PLAYER]", args[0]));
                } else sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            sendMessage(player, message.getString(Messages.COMMAND_MONEY_OTHER).replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY))
                    .replace("[MONEY]", decimalFormat.format(economy.getMoney(target.getUniqueId()))).replace("[PLAYER]", target.getName()));
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabCompleteOfflinePlayers(sender, args, 1);
    }
}
