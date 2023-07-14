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

import java.util.List;

public class HomeCommand extends CommandManager {
    public HomeCommand(Main main) {
        super(main);
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

        if (!(homes.hasHome(player, args[0]))) {
            sendMessage(player, message.getString(Messages.COMMAND_NOT_EXIST_HOME).replace("[HOME]", args[0]));
            return true;
        }

        player.teleport(homes.getHome(player, args[0]));
        sendMessage(player, message.getString(Messages.COMMAND_SEND_HOME).replace("[HOME]", args[0]));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabComplete(homes.getHomeList(), args, 1);
    }
}
