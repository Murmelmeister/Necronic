package de.murmelmeister.citybuild.command.commands.economy;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class MoneyCommand extends CommandManager {
    public MoneyCommand(CityBuild plugin) {
        super(plugin);
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

        int userId = user.getId(player.getUniqueId());

        if (args.length == 0) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_MONEY_USE))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_MONEY_USE))) return true;
            sendMessage(player, message.getString(Messages.COMMAND_MONEY_USE)
                    .replace("[MONEY]", economy.getFormattedMoney(userId, config.getString(Configs.PATTERN_DECIMAL))));
        } else if (args.length == 1) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_MONEY_OTHER))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_MONEY_OTHER))) return true;

            String target = args[0];
            getUUIDAsync(sender, target).thenAccept(uuid -> {
                if (uuid == null) return;
                if (!existUser(sender, uuid, target)) return;
                int targetId = user.getId(uuid);

                sendMessage(player, message.getString(Messages.COMMAND_MONEY_OTHER)
                        .replace("[MONEY]", economy.getFormattedMoney(targetId, config.getString(Configs.PATTERN_DECIMAL)))
                        .replace("[PLAYER]", target));
            });
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabComplete(user.getUsernames(), args, 1);
    }
}
