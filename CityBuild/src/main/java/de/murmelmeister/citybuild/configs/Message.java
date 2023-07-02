package de.murmelmeister.citybuild.configs;

import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.util.ConfigUtil;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Messages;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class Message {
    private final Logger logger;

    private File file;
    private YamlConfiguration config;

    public Message(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
    }

    public void register() {
        create();
        load();
        save();
    }

    public void create() {
        String fileName = "message.yml";
        this.file = new File("plugins//NetherLegends//", fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        for (Messages messages : Messages.values())
            if (getString(messages) == null) set(messages);
            else getString(messages);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Messages messages) {
        config.set(messages.getPath(), messages.getValue());
    }

    public String getString(Messages messages) {
        return config.getString(HexColor.format(messages.getPath()));
    }

    public String prefix() {
        return getString(Messages.PREFIX);
    }
}
