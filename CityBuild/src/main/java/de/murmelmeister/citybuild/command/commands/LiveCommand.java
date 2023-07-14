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

import java.util.ArrayList;
import java.util.List;

public class LiveCommand extends CommandManager {
    public LiveCommand(Main main) {
        super(main);
    }

    /*
    /live
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_LIVE))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_LIVE))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (listUtil.getLive().contains(player.getUniqueId())) {
            listUtil.getLive().remove(player.getUniqueId());

            //if (ranks.getBukkitTask().isCancelled()) ranks.getBukkitTask().cancel();
            //ranks.setTabRank();

            sendMessage(player, message.getString(Messages.COMMAND_LIVE_OFF));
        } else {
            listUtil.getLive().add(player.getUniqueId());

            //if (ranks.getBukkitTask().isCancelled()) ranks.getBukkitTask().cancel();
            //ranks.setTabRank();

            sendMessage(player, message.getString(Messages.COMMAND_LIVE_ON));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
