package de.murmelmeister.citybuild.command.commands.teleport;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TpaDenyCommand extends CommandManager {
    public TpaDenyCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_TPA_DENY))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_TPA_DENY)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        Player target = player.getServer().getPlayer(args[0]);

        if (target == null) {
            sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return true;
        }

        if (listUtil.getTpa().containsKey(player.getUniqueId()) && !(listUtil.getTpaHere().containsKey(player.getUniqueId()))) {
            if (!(listUtil.getTpa().containsValue(target.getUniqueId()))) {
                sendMessage(player, message.getString(Messages.COMMAND_TPA_DENY_NO_REQUEST));
                return true;
            }

            Player targetDeny = player.getServer().getPlayer(listUtil.getTpa().get(player.getUniqueId()));

            if (targetDeny == null) {
                sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            listUtil.getTpa().remove(player.getUniqueId(), targetDeny.getUniqueId());
            sendMessage(targetDeny, message.getString(Messages.COMMAND_TPA_DENY_PLAYER).replace("[PLAYER]", player.getName()));
            sendMessage(player, message.getString(Messages.COMMAND_TPA_DENY_TARGET).replace("[PLAYER]", targetDeny.getName()));
        } else if (!(listUtil.getTpa().containsKey(player.getUniqueId())) && listUtil.getTpaHere().containsKey(player.getUniqueId())) {
            if (!(listUtil.getTpaHere().containsValue(target.getUniqueId()))) {
                sendMessage(player, message.getString(Messages.COMMAND_TPA_DENY_NO_REQUEST));
                return true;
            }

            Player targetDeny = player.getServer().getPlayer(listUtil.getTpaHere().get(player.getUniqueId()));

            if (targetDeny == null) {
                sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            listUtil.getTpaHere().remove(player.getUniqueId(), targetDeny.getUniqueId());
            sendMessage(targetDeny, message.getString(Messages.COMMAND_TPA_DENY_PLAYER).replace("[PLAYER]", player.getName()));
            sendMessage(player, message.getString(Messages.COMMAND_TPA_DENY_TARGET).replace("[PLAYER]", targetDeny.getName()));
        } else {
            listUtil.getTpa().remove(player.getUniqueId());
            listUtil.getTpaHere().remove(player.getUniqueId());
            sendMessage(player, message.getString(Messages.COMMAND_TPA_DENY_NO_REQUEST));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return playerTabComplete(sender, args, 1);
    }
}
