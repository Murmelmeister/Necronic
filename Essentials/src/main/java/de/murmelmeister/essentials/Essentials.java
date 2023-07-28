package de.murmelmeister.essentials;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.murmelmeister.essentials.api.CustomPermission;
import de.murmelmeister.essentials.command.PermissionCommand;
import de.murmelmeister.essentials.command.PlayTimeCommand;
import de.murmelmeister.essentials.config.MySQL;
import de.murmelmeister.essentials.listener.PermissionListener;
import de.murmelmeister.essentials.listener.PlayTimeListener;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.playtime.PlayTime;
import org.slf4j.Logger;

@Plugin(
        id = "essentials",
        name = "Essentials",
        version = "0.0.1-ALPHA-SNAPSHOT",
        authors = "Murmelmeister"
)
public class Essentials {

    @Inject
    private Logger logger;
    @Inject
    private ProxyServer proxyServer;

    private MySQL mySQL;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.mySQL = new MySQL(logger);
        mySQL.connected();
        Permission permission = new Permission(mySQL.getConnection());
        permission.createAllTables();
        CustomPermission.updatePermission(this, proxyServer, permission);
        permission.defaultGroup("default");
        PlayTime playTime = new PlayTime(mySQL.getConnection());
        playTime.createTable();
        proxyServer.getEventManager().register(this, new PermissionListener(permission));
        proxyServer.getCommandManager().register("permission", new PermissionCommand(permission));
        proxyServer.getEventManager().register(this, new PlayTimeListener(this, proxyServer, playTime));
        proxyServer.getCommandManager().register("playtime", new PlayTimeCommand(playTime));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        mySQL.disconnected();
    }
}
