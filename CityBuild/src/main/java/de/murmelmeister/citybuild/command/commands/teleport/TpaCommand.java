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

public class TpaCommand extends CommandManager {
    public TpaCommand(Main main) {
        super(main);
    }

    /*
    /tpa <player>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_TPA))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_TPA))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length != 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return true;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return true;
        }

        listUtil.getTpa().put(target.getUniqueId(), player.getUniqueId());
        sendMessage(target, message.getString(Messages.COMMAND_TPA_GET_REQUEST_FROM_PLAYER).replace("[PLAYER]", player.getName()).replace("[PREFIX]", message.prefix()));
        sendMessage(player, message.getString(Messages.COMMAND_TPA_SEND_REQUEST_TO_TARGET).replace("[PLAYER]", target.getName()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabCompletePlayers(sender, args, 1);
    }
}
