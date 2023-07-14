package de.murmelmeister.citybuild.command.commands;

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
import java.util.UUID;

public class GodModeCommand extends CommandManager {
    public GodModeCommand(Main main) {
        super(main);
    }

    /*
    /godMode [player]
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_GOD_MODE_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_GOD_MODE_COMMAND))) return true;

        if (args.length == 0) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_GOD_MODE_USE))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_GOD_MODE_USE))) return true;

            Player player = getPlayer(sender);
            if (!(existPlayer(sender))) return true;

            UUID uuid = player.getUniqueId();
            if (listUtil.getGodMode().contains(uuid)) {
                listUtil.getGodMode().remove(uuid);
                sendMessage(player, message.getString(Messages.COMMAND_GOD_MODE_USE_OFF));
            } else {
                listUtil.getGodMode().add(uuid);
                sendMessage(player, message.getString(Messages.COMMAND_GOD_MODE_USE_ON));
            }
        } else if (args.length == 1) {
            if (!(isEnable(sender, Configs.COMMAND_ENABLE_GOD_MODE_OTHER))) return true;
            if (!(hasPermission(sender, Configs.PERMISSION_GOD_MODE_OTHER))) return true;

            Player target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
                return true;
            }

            UUID uuid = target.getUniqueId();
            if (listUtil.getGodMode().contains(uuid)) {
                listUtil.getGodMode().remove(uuid);
                sendMessage(target, message.getString(Messages.COMMAND_GOD_MODE_USE_OFF));
                sendMessage(sender, message.getString(Messages.COMMAND_GOD_MODE_OTHER_OFF).replace("[PLAYER]", target.getName()));
            } else {
                listUtil.getGodMode().add(uuid);
                sendMessage(target, message.getString(Messages.COMMAND_GOD_MODE_USE_ON));
                sendMessage(sender, message.getString(Messages.COMMAND_GOD_MODE_OTHER_ON).replace("[PLAYER]", target.getName()));
            }
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabCompletePlayers(sender, args, 1);
    }
}
