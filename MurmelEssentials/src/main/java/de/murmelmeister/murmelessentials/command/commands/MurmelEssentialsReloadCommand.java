package de.murmelmeister.murmelessentials.command.commands;

import de.murmelmeister.murmelapi.util.MySQL;
import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.command.CommandManager;
import de.murmelmeister.murmelessentials.util.config.Configs;
import de.murmelmeister.murmelessentials.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MurmelEssentialsReloadCommand extends CommandManager {
    public MurmelEssentialsReloadCommand(Main main) {
        super(main);
    }

    /*
    /murmelEssentialsReload
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RELOAD))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_RELOAD))) return true;

        this.config.create();
        this.message.create();
        MySQL.createFile(logger, String.format("plugins//%s//", config.getString(Configs.FILE_NAME)), "mysql");
        sendMessage(sender, message.getString(Messages.COMMAND_RELOAD));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
