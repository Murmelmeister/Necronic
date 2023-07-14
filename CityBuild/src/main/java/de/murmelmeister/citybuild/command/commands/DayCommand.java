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

public class DayCommand extends CommandManager {
    public DayCommand(Main main) {
        super(main);
    }

    /*
    /day
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_TIME_DAY))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_TIME_DAY))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (player.hasPermission(config.getString(Configs.PERMISSION_NOT_COOLDOWN))) {
            player.getWorld().setTime(config.getLong(Configs.TIME_DAY_TIME));
            sendMessage(player, message.getString(Messages.COMMAND_DAY));
            return true;
        }

        if (cooldown.getDuration(player, "Day") <= System.currentTimeMillis())
            cooldown.removeCooldown(player, "Day");

        if (cooldown.hasCooldown(player, "Day")) {
            sendMessage(player, message.getString(Messages.COOLDOWN_MESSAGE).replace("[DATE]", cooldown.getExpired(player, "Day").replace(" ", message.getString(Messages.COOLDOWN_DATE))));
            return true;
        }

        cooldown.addCooldown(player, "Day", config.getLong(Configs.TIME_DAY_COOLDOWN));
        player.getWorld().setTime(config.getLong(Configs.TIME_DAY_TIME));
        sendMessage(player, message.getString(Messages.COMMAND_DAY));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
