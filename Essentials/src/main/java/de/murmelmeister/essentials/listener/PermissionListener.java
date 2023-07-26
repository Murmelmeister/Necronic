package de.murmelmeister.essentials.listener;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import de.murmelmeister.essentials.api.CustomPermission;
import de.murmelmeister.murmelapi.permission.Permission;

public class PermissionListener {
    private final Permission permission;

    public PermissionListener(Permission permission) {
        this.permission = permission;
    }

    @Subscribe
    public void onPermission(PermissionsSetupEvent event, Continuation continuation) {
        if (!(event.getSubject() instanceof Player)) {
            continuation.resume();
            return;
        }
        Player player = (Player) event.getSubject();
        try {
            event.setProvider(new CustomPermission(permission, player));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            continuation.resume();
        }
    }

    @Subscribe
    public void onConnect(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        permission.getUser().create(player.getUniqueId(), player.getUsername());
        permission.getUser().renameUsername(player.getUniqueId(), player.getUsername());
        permission.getUser().addParent(player.getUniqueId(), player.getUsername(), permission.defaultGroup());
    }
}
