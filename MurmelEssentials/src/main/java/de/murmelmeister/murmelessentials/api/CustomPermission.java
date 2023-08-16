package de.murmelmeister.murmelessentials.api;

import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.permission.PermissionConfig;
import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;

public class CustomPermission extends PermissibleBase {
    private final Player player;
    private final Permission permission;

    public CustomPermission(Player player, Permission permission) {
        super(player);
        this.player = player;
        this.permission = permission;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return this.permission.hasPermission(player.getUniqueId(), permission);
    }

    public static void updatePermission(Main main) {
        main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(main.getInstance(), () -> main.getInstance().getServer().getOnlinePlayers().forEach(Player::updateCommands), 20L, main.getPermission().getValueLong(PermissionConfig.UPDATE_SCHEDULE_PERMISSION_COMMAND) * 20L);
    }

    @Deprecated
    public static void updateRankPermission(Main main) {
        MurmelEssentials instance = main.getInstance();
        Permission perms = main.getPermission();
        Group group = perms.getGroup();
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> group.groups().forEach(s -> {
            String defaultPermission = perms.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + s;
            group.addPermission(s, defaultPermission, -1);
            group.groups().forEach(s1 -> {
                String defaultPermission1 = perms.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + s1;
                if (s.equals(perms.defaultGroup())) return;
                if (defaultPermission.equals(defaultPermission1)) return;
                group.addPermission(s, "-" + defaultPermission1, -1);
            });
        }), 20L, 60L * 20L);
    }
}
