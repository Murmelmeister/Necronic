package de.murmelmeister.citybuild.command.commands.locations;

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

public class SpawnCommand extends CommandManager {
    public SpawnCommand(Main main) {
        super(main);
    }

    /*
    /spawn
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_SPAWN))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_SPAWN))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        if (!(locations.isSpawnExist())) {
            sendMessage(player, message.getString(Messages.COMMAND_LOCATION_NOT_EXIST).replace("[LOCATION]", "Spawn"));
            return true;
        }

        player.teleport(locations.getSpawn());
        sendMessage(player, message.getString(Messages.COMMAND_SEND_SPAWN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
