package de.murmelmeister.citybuild.command.commands.homes;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.api.home.HomeImpl;
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
    public AddHomeCommand(CityBuild plugin) {
        super(plugin);
    }

    /*
    /addHome <home>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ADD_HOME))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_ADD_HOME))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        int userId = user.createOrGetUser(player.getUniqueId());

        String homeName = args[0];
        if (homes.existsHome(userId, homeName)) {
            sendMessage(player, message.getString(Messages.COMMAND_EXIST_HOME).replace("[HOME]", homeName));
            return true;
        }

        if (homeLimit(player, userId)) {
            homes.addHome(new HomeImpl(userId, homeName, player.getLocation()));
            sendMessage(player, message.getString(Messages.COMMAND_ADD_HOME).replace("[HOME]", homeName));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    private boolean homeLimit(Player player, int userId) {
        if (player.hasPermission(config.getString(Configs.PERMISSION_HOME_UNLIMITED))) return true;

        int limit;
        if (player.hasPermission(config.getString(Configs.PERMISSION_HOME_LIMIT_TEAM))) limit = config.getInt(Configs.HOME_LIMIT_TEAM);
        else if (player.hasPermission(config.getString(Configs.PERMISSION_HOME_LIMIT_RANK))) limit = config.getInt(Configs.HOME_LIMIT_RANK);
        else limit = config.getInt(Configs.HOME_LIMIT_DEFAULT);

        if (homes.getHomeNames(userId).size() >= limit) {
            sendMessage(player, message.getString(Messages.COMMAND_HOME_LIMIT));
            return false;
        }
        return true;
    }
}
