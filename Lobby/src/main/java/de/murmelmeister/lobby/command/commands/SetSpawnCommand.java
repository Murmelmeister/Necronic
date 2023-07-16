package de.murmelmeister.lobby.command.commands;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.command.CommandManager;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.lobby.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand extends CommandManager {
    public SetSpawnCommand(Main main) {
        super(main);
    }

    /*
    /setSpawn
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_SET_SPAWN))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_SET_SPAWN))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        locations.setLocation(player.getLocation(), "Spawn");
        sendMessage(player, message.getString(Messages.COMMAND_SET_SPAWN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
