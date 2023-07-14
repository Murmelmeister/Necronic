package de.murmelmeister.citybuild.command.commands.locations;

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

public class WarpCommand extends CommandManager {
    public WarpCommand(Main main) {
        super(main);
    }

    /*
    /warp <location>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_LOCATION))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_LOCATION))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        if (!(locations.hasLocation(args[0]))) {
            sendMessage(player, message.getString(Messages.COMMAND_LOCATION_NOT_EXIST).replace("[LOCATION]", args[0]));
            return true;
        }

        player.teleport(locations.getLocation(args[0]));
        sendMessage(player, message.getString(Messages.COMMAND_SEND_LOCATION).replace("[LOCATION]", args[0]));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabComplete(locations.getLocationList(), args, 1);
    }
}
