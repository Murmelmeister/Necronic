package de.murmelmeister.citybuild;

import org.bukkit.plugin.java.JavaPlugin;

public final class CityBuild extends JavaPlugin {
    private Main main;

    @Override
    public void onLoad() {
        this.main = new Main(this);
    }

    @Override
    public void onDisable() {
        main.disable();
    }

    @Override
    public void onEnable() {
        main.enable();
    }
}
