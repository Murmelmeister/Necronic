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

import java.util.Collections;
import java.util.List;

public class TpCommand extends CommandManager {
    public TpCommand(Main main) {
        super(main);
    }

    /*
    /tp <player> [player]
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_TP))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_TP))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length == 1) {
            Player target = player.getServer().getPlayer(args[0]);
            if (target == null) {
                sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            // player teleport to target
            player.teleport(target);
            sendMessage(player, message.getString(Messages.COMMAND_TP_TO_TARGET).replace("[PLAYER]", target.getName()));
        } else if (args.length == 2) {
            Player target1 = player.getServer().getPlayer(args[0]);
            if (target1 == null) {
                sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            Player target2 = player.getServer().getPlayer(args[1]);
            if (target2 == null) {
                sendMessage(player, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[1]));
                return true;
            }

            // target1 teleport to target2
            target1.teleport(target2);
            sendMessage(target1, message.getString(Messages.COMMAND_TP_TO_TARGET).replace("[PLAYER]", target2.getName()));
            sendMessage(player, message.getString(Messages.COMMAND_TP_TARGET_TO_TARGET).replace("[PLAYER1]", target1.getName()).replace("[PLAYER2]", target2.getName()));
        } else sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return tabCompletePlayers(sender, args);
        if (args.length == 2) return tabCompletePlayers(sender, args);
        return Collections.emptyList();
    }
}
