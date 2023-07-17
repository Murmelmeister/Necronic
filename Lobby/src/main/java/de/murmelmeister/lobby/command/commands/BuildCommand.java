package de.murmelmeister.lobby.command.commands;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.command.CommandManager;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.lobby.util.config.Messages;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BuildCommand extends CommandManager {
    public BuildCommand(Main main) {
        super(main);
    }

    /*
    /build [player]
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_BUILD_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_BUILD_COMMAND))) return true;

        if (args.length == 0) {
            if (!(isEnable(sender, Configs.COMMAND_BUILD_USE))) return true;
            if (!(hasPermission(sender,Configs.PERMISSION_BUILD_USE))) return true;

            Player player = getPlayer(sender);
            if (!(existPlayer(sender))) return true;

            if (listUtil.getBuild().contains(player.getUniqueId())) {
                listUtil.getBuild().remove(player.getUniqueId());
                player.setGameMode(GameMode.SURVIVAL); // TODO: Add this to the config
                // TODO: Message
            } else {
                listUtil.getBuild().add(player.getUniqueId());
                player.setGameMode(GameMode.CREATIVE);
                // TODO: Message
            }
        } else if (args.length == 1) {
            if (!(isEnable(sender, Configs.COMMAND_BUILD_OTHER))) return true;
            if (!(hasPermission(sender,Configs.PERMISSION_BUILD_OTHER))) return true;

            Player target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            if (listUtil.getBuild().contains(target.getUniqueId())) {
                listUtil.getBuild().remove(target.getUniqueId());
                target.setGameMode(GameMode.SURVIVAL); // TODO: Add this to the config
                // TODO: Message
            } else {
                listUtil.getBuild().add(target.getUniqueId());
                target.setGameMode(GameMode.CREATIVE);
                // TODO: Message
            }
        } else {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
