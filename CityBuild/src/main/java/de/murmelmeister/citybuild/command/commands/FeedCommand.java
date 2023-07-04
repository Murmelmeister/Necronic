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

public class FeedCommand extends CommandManager {
    public FeedCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_FEED_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_FEED_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        if (args.length == 0) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_FEED_USE))) {
                sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(sender.hasPermission(config.getString(Configs.PERMISSION_FEED_USE)))) {
                sendMessage(sender, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player player = sender instanceof Player ? (Player) sender : null;

            if (player == null) {
                sendMessage(sender, message.getString(Messages.NO_CONSOLE));
                return true;
            }

            player.setFoodLevel(20);
            sendMessage(player, message.getString(Messages.COMMAND_FEED_USE));

        } else if (args.length == 1) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_FEED_OTHER))) {
                sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(sender.hasPermission(config.getString(Configs.PERMISSION_FEED_OTHER)))) {
                sendMessage(sender, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player target = sender.getServer().getPlayer(args[0]);

            if (target == null) {
                sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            target.setFoodLevel(20);
            sendMessage(target, message.getString(Messages.COMMAND_FEED_USE));
            sendMessage(sender, message.getString(Messages.COMMAND_FEED_OTHER).replace("[PLAYER]", target.getName()));

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
