package de.murmelmeister.citybuild.listener;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class Listeners implements Listener {
    public final CityBuild instance;

    public final Config config;
    public final Message message;

    public Listeners(Main main) {
        this.instance = main.getInstance();
        this.config = main.getConfig();
        this.message = main.getMessage();
    }

    public void register() {

    }

    private void addListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public void sendMessage(CommandSender sender, String message) {
        if (config.getBoolean(Configs.PREFIX_ENABLE))
            sender.sendMessage(this.message.prefix() + HexColor.format(message));
        else sender.sendMessage(HexColor.format(message));
    }
}
