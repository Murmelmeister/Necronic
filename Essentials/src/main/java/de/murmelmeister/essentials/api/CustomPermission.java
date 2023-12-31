package de.murmelmeister.essentials.api;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.murmelmeister.essentials.Essentials;
import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.permission.PermissionConfig;

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
        proxyServer.getScheduler().buildTask(instance, permission::expired).repeat(permission.getValueLong(PermissionConfig.UPDATE_SCHEDULE_RANK_EXPIRED) * 1000L, TimeUnit.MILLISECONDS).schedule();
    }

    public static void updateRankPermission(Permission permission) {
        Group group = permission.getGroup();
        group.groups().forEach(s -> group.addPermission(s, permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + s, -1));
    }
}
