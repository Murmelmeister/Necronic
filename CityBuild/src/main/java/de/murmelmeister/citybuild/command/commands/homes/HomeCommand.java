package de.murmelmeister.citybuild.command.commands.homes;

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

public class HomeCommand extends CommandManager {
    public HomeCommand(CityBuild plugin) {
        super(plugin);
    }

    /*
    /home <home>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_HOME))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_HOME))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        int userId = user.createOrGetUser(player.getUniqueId());

        String homeName = args[0];
        if (!(homes.existsHome(userId, homeName))) {
            sendMessage(player, message.getString(Messages.COMMAND_NOT_EXIST_HOME).replace("[HOME]", homeName));
            return true;
        }

        player.teleport(homes.getHome(player.getServer(), userId, homeName));
        sendMessage(player, message.getString(Messages.COMMAND_SEND_HOME).replace("[HOME]", homeName));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = getPlayer(sender);
        int userId = user.createOrGetUser(player.getUniqueId());
        return tabComplete(homes.getHomeNames(userId), args, 1);
    }
}
