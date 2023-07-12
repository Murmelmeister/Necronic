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

public class HealCommand extends CommandManager {
    public HealCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_HEAL_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_HEAL_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        if (args.length == 0) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_HEAL_USE))) {
                sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(sender.hasPermission(config.getString(Configs.PERMISSION_HEAL_USE)))) {
                sendMessage(sender, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player player = sender instanceof Player ? (Player) sender : null;

            if (player == null) {
                sendMessage(sender, message.getString(Messages.NO_CONSOLE));
                return true;
            }

            player.setHealth(20.0D);
            player.setFoodLevel(20);
            sendMessage(player, message.getString(Messages.COMMAND_HEAL_USE));

        } else if (args.length == 1) {
            if (!(config.getBoolean(Configs.COMMAND_ENABLE_HEAL_OTHER))) {
                sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
                return true;
            }

            if (!(sender.hasPermission(config.getString(Configs.PERMISSION_HEAL_OTHER)))) {
                sendMessage(sender, message.getString(Messages.NO_PERMISSION));
                return true;
            }

            Player target = sender.getServer().getPlayer(args[0]);

            if (target == null) {
                sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            target.setHealth(20.0D);
            target.setFoodLevel(20);
            sendMessage(target, message.getString(Messages.COMMAND_HEAL_USE));
            sendMessage(sender, message.getString(Messages.COMMAND_HEAL_OTHER).replace("[PLAYER]", target.getName()));

        } else {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return playerTabComplete(sender, args, 1);
    }
}
