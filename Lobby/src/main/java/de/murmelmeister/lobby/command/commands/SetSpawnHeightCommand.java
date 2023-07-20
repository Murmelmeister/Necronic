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

public class SetSpawnHeightCommand extends CommandManager {
    public SetSpawnHeightCommand(Main main) {
        super(main);
    }

    /*
    /setSpawnHeight
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_SET_SPAWN_HEIGHT))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_SET_SPAWN_HEIGHT))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        locations.setSpawnHeight(player.getLocation());
        sendMessage(player, message.getString(Messages.COMMAND_SET_SPAWN_HEIGHT).replace("[HEIGHT]", String.valueOf(player.getLocation().getBlockY())));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
