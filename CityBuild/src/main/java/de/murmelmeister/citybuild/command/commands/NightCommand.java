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


public class NightCommand extends CommandManager {
    public NightCommand(Main main) {
        super(main);
    }

    /*
    /night
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_TIME_NIGHT))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_TIME_NIGHT))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (player.hasPermission(config.getString(Configs.PERMISSION_NOT_COOLDOWN))) {
            player.getWorld().setTime(config.getLong(Configs.TIME_NIGHT_TIME));
            sendMessage(player, message.getString(Messages.COMMAND_NIGHT));
            return true;
        }

        if (cooldown.getDuration(player.getUniqueId(), "Night") <= System.currentTimeMillis())
            cooldown.removeCooldown(player.getUniqueId(), "Night");

        if (cooldown.hasCooldown(player.getUniqueId(), "Night")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player.getUniqueId(), "Night").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return true;
        }

        cooldown.addCooldown(player.getUniqueId(), "Night", config.getLong(Configs.TIME_NIGHT_COOLDOWN));
        player.getWorld().setTime(config.getLong(Configs.TIME_NIGHT_TIME));
        sendMessage(player, message.getString(Messages.COMMAND_NIGHT));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
