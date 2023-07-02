package de.murmelmeister.citybuild.command.commands.homes;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AddHomeCommand extends CommandManager {
    public AddHomeCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_ADD_HOME))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_ADD_HOME)))) {
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

        if (homes.hasHome(player, args[0])) {
            sendMessage(player, message.getString(Messages.COMMAND_EXIST_HOME).replace("[HOME]", args[0]));
            return true;
        }

        if (homeLimit(player)) {
            homes.addHome(player, args[0]);
            sendMessage(player, message.getString(Messages.COMMAND_ADD_HOME).replace("[HOME]", args[0]));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private boolean homeLimit(Player player) {
        if (player.hasPermission(config.getString(Configs.PERMISSION_HOME_UNLIMITED))) return true;

        int limit;
        if (player.hasPermission(config.getString(Configs.PERMISSION_HOME_LIMIT_TEAM))) limit = config.getInt(Configs.HOME_LIMIT_TEAM);
        else if (player.hasPermission(config.getString(Configs.PERMISSION_HOME_LIMIT_RANK))) limit = config.getInt(Configs.HOME_LIMIT_RANK);
        else limit = config.getInt(Configs.HOME_LIMIT_DEFAULT);

        if (homes.getHomeList().size() >= limit) {
            sendMessage(player, message.getString(Messages.COMMAND_HOME_LIMIT));
            return false;
        }
        return true;
    }
}
