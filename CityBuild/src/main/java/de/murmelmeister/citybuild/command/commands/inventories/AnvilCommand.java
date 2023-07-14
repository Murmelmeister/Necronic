package de.murmelmeister.citybuild.command.commands.inventories;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.command.CommandManager;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnvilCommand extends CommandManager {
    public AnvilCommand(Main main) {
        super(main);
    }

    /*
    /anvil
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_ANVIL))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_ANVIL))) return true;

        Player player = getPlayer(sender);
        if (!(existPlayer(sender))) return true;

        player.openAnvil(null, true);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
