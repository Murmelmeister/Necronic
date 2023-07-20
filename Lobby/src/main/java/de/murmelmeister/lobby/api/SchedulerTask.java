package de.murmelmeister.lobby.api;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.configs.Config;
import de.murmelmeister.lobby.util.ConfigUtil;
import de.murmelmeister.lobby.util.config.Configs;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchedulerTask {
    private final Logger logger;
    private final Config defaultConfig;

    private File file;
    private YamlConfiguration config;

    private List<String> taskList;

    public SchedulerTask(Main main) {
        this.logger = main.getInstance().getSLF4JLogger();
        this.defaultConfig = main.getConfig();
    }

    public void create(Player player) {
        String fileName = player.getUniqueId() + ".yml";
        this.file = new File(String.format("plugins//%s//SchedulerTask//", defaultConfig.getString(Configs.FILE_NAME)), fileName);
        ConfigUtil.createFile(logger, file, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setUsername(Player player) {
        create(player);
        set("Username", player.getName());
        save();
    }

    public void addBukkitTask(Player player, BukkitTask bukkitTask) {
        create(player);
        this.taskList = getTaskList();
        taskList.add(bukkitTask.toString());
        set("TaskList", taskList);
        save();
    }

    public void removeBukkitTask(Player player, BukkitTask bukkitTask) {
        create(player);
        this.taskList = getTaskList();
        if (taskList.contains(bukkitTask.toString())) bukkitTask.cancel();
        taskList.remove(bukkitTask.toString());
        set("TaskList", taskList);
        save();
    }

    public void clearBukkitTask(Player player) {
        create(player);
        this.taskList = getTaskList();
        taskList.clear();
        set("TaskList", taskList);
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getTaskList() {
        this.taskList = new ArrayList<>();
        if (config.contains("TaskList")) taskList = config.getStringList("TaskList");
        return taskList;
    }

    private void set(String path, Object value) {
        config.set(path, value);
    }
}
