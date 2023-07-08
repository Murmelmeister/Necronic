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

import java.util.*;

public class NightCommand extends CommandManager {
    public NightCommand(Main main) {
        super(main);
    }

    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_TIME_NIGHT))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_TIME_NIGHT)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return true;
        }

        long current = System.currentTimeMillis();
        if (cooldown.containsKey(player.getUniqueId())) {
            long past = cooldown.get(player.getUniqueId());
            int result = (int) ((past + (config.getLong(Configs.TIME_NIGHT_COOLDOWN))) - current);
            if (result > 0) {
                int minute = result / 60 / 1000;
                result = result - (minute * 1000 * 60);
                int second = result / 1000;
                sendMessage(player, minute + " min " + second + " sec");
                return true;
            }
        }

        cooldown.put(player.getUniqueId(), current);
        player.getWorld().setTime(config.getLong(Configs.TIME_NIGHT_TIME));
        sendMessage(player, message.getString(Messages.COMMAND_NIGHT));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
