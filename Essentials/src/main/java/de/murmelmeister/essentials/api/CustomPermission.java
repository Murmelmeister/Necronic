package de.murmelmeister.essentials.api;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.murmelmeister.essentials.Essentials;
import de.murmelmeister.murmelapi.permission.Permission;

import java.util.concurrent.TimeUnit;

public class CustomPermission implements PermissionProvider, PermissionFunction {
    private final Permission permission;
    private final Player player;

    public CustomPermission(Permission permission, Player player) {
        this.permission = permission;
        this.player = player;
    }


    @Override
    public Tristate getPermissionValue(String permission) {
        return Tristate.fromBoolean(this.permission.hasPermission(player.getUniqueId(), permission));
    }

    @Override
    public PermissionFunction createFunction(PermissionSubject subject) {
        Preconditions.checkState(subject == this.player);
        return this;
    }

    public static void updatePermission(Essentials instance, ProxyServer proxyServer, Permission permission) {
        proxyServer.getScheduler().buildTask(instance, permission::expired).repeat(100L, TimeUnit.MILLISECONDS).schedule();
    }
}
