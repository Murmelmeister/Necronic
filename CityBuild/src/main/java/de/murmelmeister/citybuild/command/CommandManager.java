package de.murmelmeister.citybuild.command;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public abstract class CommandManager extends Commands implements TabExecutor {
    public final Config config;
    public final Message message;

    public CommandManager(Main main) {
        super(main);
        this.config = main.getConfig();
        this.message = main.getMessage();
    }

    public void sendMessage(CommandSender sender, String message) {
        if (config.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(this.message.prefix() + HexColor.format(message));
        else sender.sendMessage(HexColor.format(message));
    }
}
