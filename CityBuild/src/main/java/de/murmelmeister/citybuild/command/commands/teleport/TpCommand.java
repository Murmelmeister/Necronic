package de.murmelmeister.citybuild.command.commands.teleport;

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

import java.util.ArrayList;
import java.util.List;

public class TpCommand extends CommandManager {
    public TpCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_TP))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_TP)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

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

        } else {
            sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (args.length == 1) {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            for (Player player : sender.getServer().getOnlinePlayers()) {
                String name = player.getName();
                if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord))
                    tabComplete.add(name);
            }
            tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
        }

        if (args.length == 2) {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            for (Player player : sender.getServer().getOnlinePlayers()) {
                String name = player.getName();
                if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord))
                    tabComplete.add(name);
            }
            tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return tabComplete;
    }
}
