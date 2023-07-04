package de.murmelmeister.citybuild.command.commands;

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

public class FlyCommand extends CommandManager {
    public FlyCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_FLY_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_FLY_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        if (args.length == 0) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_FLY_USE))) {
                sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(sender.hasPermission(config.getString(Configs.PERMISSION_FLY_USE)))) {
                sendMessage(sender, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player player = sender instanceof Player ? (Player) sender : null;

            if (player == null) {
                sendMessage(sender, message.getString(Messages.NO_CONSOLE));
                return true;
            }

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.setFlying(false);
                sendMessage(player, message.getString(Messages.COMMAND_FLY_USE_OFF));
            } else {
                player.setAllowFlight(true);
                player.setFlying(true);
                sendMessage(player, message.getString(Messages.COMMAND_FLY_USE_ON));
            }

        } else if (args.length == 1) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_FLY_OTHER))) {
                sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(sender.hasPermission(config.getString(Configs.PERMISSION_FLY_OTHER)))) {
                sendMessage(sender, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player target = sender.getServer().getPlayer(args[0]);

            if (target == null) {
                sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            if (target.getAllowFlight()) {
                target.setAllowFlight(false);
                target.setFlying(false);
                sendMessage(target, message.getString(Messages.COMMAND_FLY_USE_OFF));
                sendMessage(sender, message.getString(Messages.COMMAND_FLY_OTHER_OFF).replace("[PLAYER]", target.getName()));
            } else {
                target.setAllowFlight(true);
                target.setFlying(true);
                sendMessage(target, message.getString(Messages.COMMAND_FLY_USE_ON));
                sendMessage(sender, message.getString(Messages.COMMAND_FLY_OTHER_ON).replace("[PLAYER]", target.getName()));
            }

        } else {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return playerTabComplete(sender, args);
    }
}
