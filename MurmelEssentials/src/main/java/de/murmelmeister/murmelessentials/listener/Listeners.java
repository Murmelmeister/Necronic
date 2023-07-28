package de.murmelmeister.murmelessentials.listener;

import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import de.murmelmeister.murmelessentials.listener.listeners.PermissionListener;
import org.bukkit.event.Listener;

public class Listeners implements Listener {
    public final Main main;
    public final MurmelEssentials instance;

    public Listeners(Main main) {
        this.main = main;
        this.instance = main.getInstance();
    }

    public void register() {
        addListener(new PermissionListener(main));
    }

    private void addListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }
}
