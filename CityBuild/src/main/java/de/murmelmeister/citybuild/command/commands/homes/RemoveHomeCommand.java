package de.murmelmeister.citybuild.command.commands.homes;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveHomeCommand extends CommandManager {
    public RemoveHomeCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_REMOVE_HOME))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_REMOVE_HOME)))) {
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

        if (!(homes.hasHome(player, args[0]))) {
            sendMessage(player, message.getString(Messages.COMMAND_NOT_EXIST_HOME).replace("[HOME]", args[0]));
            return true;
        }

        homes.removeHome(player, args[0]);
        sendMessage(player, message.getString(Messages.COMMAND_REMOVE_HOME).replace("[HOME]", args[0]));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return homes.getHomeList().stream().filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        return Collections.emptyList();
    }
}
