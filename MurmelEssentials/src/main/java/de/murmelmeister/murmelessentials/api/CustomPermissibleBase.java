package de.murmelmeister.murmelessentials.api;

import de.murmelmeister.murmelapi.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;

public class CustomPermissibleBase extends PermissibleBase {
    private final Player player;
    private final Permission permission;

    public CustomPermissibleBase(Player player, Permission permission) {
        super(player);
        this.player = player;
        this.permission = permission;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return this.permission.hasPermission(player.getUniqueId(), permission);
    }
}
