package de.murmelmeister.murmelessentials.api;

import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelessentials.Main;
import de.murmelmeister.murmelessentials.MurmelEssentials;
import de.murmelmeister.murmelessentials.configs.Config;
import de.murmelmeister.murmelessentials.util.config.Configs;
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
        main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(main.getInstance(), () -> {
            main.getInstance().getServer().getOnlinePlayers().forEach(Player::updateCommands);
        }, 20L, main.getConfig().getLong(Configs.UPDATE_SCHEDULE_PERMISSION_COMMAND) * 20L);
    }

    public static void updateRankPermission(Main main) {
        MurmelEssentials instance = main.getInstance();
        Config config = main.getConfig();
        Permission perms = main.getPermission();
        Group group = perms.getGroup();
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> group.groups().forEach(s -> {
            String defaultPermission = config.getString(Configs.DEFAULT_PRE_PERMISSION) + s;
            group.addPermission(s, defaultPermission);
            group.groups().forEach(s1 -> {
                String defaultPermission1 = config.getString(Configs.DEFAULT_PRE_PERMISSION) + s1;
                if (s.equals(perms.defaultGroup())) return;
                if (defaultPermission.equals(defaultPermission1)) return;
                group.addPermission(s, "-" + defaultPermission1);
            });
        }), 20L, config.getLong(Configs.UPDATE_SCHEDULE_RANK_PERMISSION) * 20L);
    }
}
