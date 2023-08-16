package de.murmelmeister.citybuild.command.commands;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.command.Commands;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SettingsCommand extends CommandManager {
    public SettingsCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_SETTINGS))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_SETTINGS))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (args.length >= 1) {
            switch (args[0]) {
                case "scoreboard":
                    settingScoreboard(player, command, args);
                    break;
                default:
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                    break;
            }
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return Stream.of("scoreboard").filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 2 && args[0].equals("scoreboard"))
            return Stream.of("enable", "disable").filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        return Collections.emptyList();
    }

    private void settingScoreboard(Player player, Command command, String[] args) {
        String path = "Settings.Scoreboard";
        String mode = settings.getBoolean(player.getUniqueId(), path) ? "enable" : "disable";
        if (args.length == 1) {
            sendMessage(player, message.getString(Messages.COMMAND_SETTINGS_SCOREBOARD).replace("[MODE]", mode));
            return;
        }

        switch (args[1]) {
            case "enable":
                if (settings.getBoolean(player.getUniqueId(), path)) {
                    sendMessage(player, message.getString(Messages.COMMAND_SETTINGS_SCOREBOARD).replace("[MODE]", mode));
                    return;
                }
                settings.setEnableScoreboard(player.getUniqueId(), true);
                sendMessage(player, message.getString(Messages.SETTINGS_DISABLE_SCOREBOARD));
                break;
            case "disable":
                if (!(settings.getBoolean(player.getUniqueId(), path))) {
                    sendMessage(player, message.getString(Messages.COMMAND_SETTINGS_SCOREBOARD).replace("[MODE]", mode));
                    return;
                }
                settings.setEnableScoreboard(player.getUniqueId(), false);
                sendMessage(player, message.getString(Messages.SETTINGS_DISABLE_SCOREBOARD));
                break;
            default:
                sendMessage(player, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                break;
        }
    }
}
