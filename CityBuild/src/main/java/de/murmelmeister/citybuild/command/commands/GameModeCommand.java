package de.murmelmeister.citybuild.command.commands;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModeCommand extends CommandManager {
    public GameModeCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_COMMAND))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return true;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_COMMAND)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return true;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "survival":
                    gameModeUseSurvival(sender);
                    break;
                case "creative":
                    gameModeUseCreative(sender);
                    break;
                case "adventure":
                    gameModeUseAdventure(sender);
                    break;
                case "spectator":
                    gameModeUseSpectator(sender);
                    break;
                case "0":
                    gameModeUseSurvival(sender);
                    break;
                case "1":
                    gameModeUseCreative(sender);
                    break;
                case "2":
                    gameModeUseAdventure(sender);
                    break;
                case "3":
                    gameModeUseSpectator(sender);
                    break;
                default:
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                    break;
            }
        } else if (args.length == 2) {
            switch (args[0]) {
                case "survival":
                    gameModeOtherSurvival(sender, args);
                    break;
                case "creative":
                    gameModeOtherCreative(sender, args);
                    break;
                case "adventure":
                    gameModeOtherAdventure(sender, args);
                    break;
                case "spectator":
                    gameModeOtherSpectator(sender, args);
                    break;
                case "0":
                    gameModeOtherSurvival(sender, args);
                    break;
                case "1":
                    gameModeOtherCreative(sender, args);
                    break;
                case "2":
                    gameModeOtherAdventure(sender, args);
                    break;
                case "3":
                    gameModeOtherSpectator(sender, args);
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
            return Stream.of("survival", "creative", "adventure", "spectator", "0", "1", "2", "3").filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 2) {
            List<String> tabComplete = new ArrayList<>();
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            for (Player player : sender.getServer().getOnlinePlayers()) {
                String name = player.getName();
                if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord))
                    tabComplete.add(name);
            }
            tabComplete.sort(String.CASE_INSENSITIVE_ORDER);
            return tabComplete;
        }
        return Collections.emptyList();
    }

    private void gameModeUseSurvival(CommandSender sender) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_USE_SURVIVAL))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_USE_SURVIVAL)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return;
        }

        player.setGameMode(GameMode.SURVIVAL);
        sendMessage(player, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.SURVIVAL.name()));
    }

    private void gameModeUseCreative(CommandSender sender) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_USE_CREATIVE))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_USE_CREATIVE)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return;
        }

        player.setGameMode(GameMode.CREATIVE);
        sendMessage(player, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.CREATIVE.name()));
    }

    private void gameModeUseAdventure(CommandSender sender) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_USE_ADVENTURE))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_USE_ADVENTURE)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return;
        }

        player.setGameMode(GameMode.ADVENTURE);
        sendMessage(player, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.ADVENTURE.name()));
    }

    private void gameModeUseSpectator(CommandSender sender) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_USE_SPECTATOR))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_USE_SPECTATOR)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player player = sender instanceof Player ? (Player) sender : null;

        if (player == null) {
            sendMessage(sender, message.getString(Messages.NO_CONSOLE));
            return;
        }

        player.setGameMode(GameMode.SPECTATOR);
        sendMessage(player, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.SPECTATOR.name()));
    }

    private void gameModeOtherSurvival(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_OTHER_SURVIVAL))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_OTHER_SURVIVAL)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[0]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return;
        }

        target.setGameMode(GameMode.SURVIVAL);
        sendMessage(target, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.SURVIVAL.name()));
        sendMessage(sender, message.getString(Messages.COMMAND_GAME_MODE_OTHER).replace("[MODE]", GameMode.SURVIVAL.name()).replace("[PLAYER]", target.getName()));
    }

    private void gameModeOtherCreative(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_OTHER_CREATIVE))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_OTHER_CREATIVE)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[0]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return;
        }

        target.setGameMode(GameMode.CREATIVE);
        sendMessage(target, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.CREATIVE.name()));
        sendMessage(sender, message.getString(Messages.COMMAND_GAME_MODE_OTHER).replace("[MODE]", GameMode.CREATIVE.name()).replace("[PLAYER]", target.getName()));
    }

    private void gameModeOtherAdventure(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_OTHER_ADVENTURE))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_OTHER_ADVENTURE)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[0]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return;
        }

        target.setGameMode(GameMode.ADVENTURE);
        sendMessage(target, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.ADVENTURE.name()));
        sendMessage(sender, message.getString(Messages.COMMAND_GAME_MODE_OTHER).replace("[MODE]", GameMode.ADVENTURE.name()).replace("[PLAYER]", target.getName()));
    }

    private void gameModeOtherSpectator(CommandSender sender, String[] args) {
        if (!(config.getBoolean(Configs.COMMAND_ENABLE_GAME_MODE_OTHER_SPECTATOR))) {
            sendMessage(sender, message.getString(Messages.DISABLE_COMMAND));
            return;
        }

        if (!(sender.hasPermission(config.getString(Configs.PERMISSION_GAME_MODE_OTHER_SPECTATOR)))) {
            sendMessage(sender, message.getString(Messages.NO_PERMISSION));
            return;
        }

        Player target = sender.getServer().getPlayer(args[0]);

        if (target == null) {
            sendMessage(sender, message.getString(Messages.NO_PLAYER_EXIST).replace("[PLAYER]", args[0]));
            return;
        }

        target.setGameMode(GameMode.SPECTATOR);
        sendMessage(target, message.getString(Messages.COMMAND_GAME_MODE_USE).replace("[MODE]", GameMode.SPECTATOR.name()));
        sendMessage(sender, message.getString(Messages.COMMAND_GAME_MODE_OTHER).replace("[MODE]", GameMode.SPECTATOR.name()).replace("[PLAYER]", target.getName()));
    }
}
