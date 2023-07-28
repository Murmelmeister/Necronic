package de.murmelmeister.essentials.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.permission.User;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.UUID;

public class PermissionCommand implements SimpleCommand {
    private final Permission permission;

    public PermissionCommand(Permission permission) {
        this.permission = permission;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        Group group = permission.getGroup();
        User user = permission.getUser();

        if (!(source.hasPermission("essentials.command.permission"))) {
            source.sendMessage(Component.text("§cYou don't have the right permission to do that."));
            return;
        }

        if (args.length >= 3) {
            switch (args[0]) {
                case "users":
                    users(source, user, args);
                    break;
                case "groups":
                    groups(source, group, args);
                    break;
                default:
                    syntax(source);
                    break;
            }
        } else syntax(source);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    private void users(CommandSource source, User user, String[] args) {
        String username = args[1];
        if (!(user.convertUsernameToUUID(username))) {
            source.sendMessage(Component.text(String.format("§cThe player §e%s§c does not exist.", username)));
            return;
        }

        UUID uuid = user.uuid(username);
        switch (args[2]) {
            case "parent":
                userParent(source, uuid, username, user, args);
                break;
            case "permission":
                userPermission(source, uuid, username, user, args);
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void userParent(CommandSource source, UUID uuid, String username, User user, String[] args) {
        if (args.length == 3) {
            source.sendMessage(Component.text("§3Parents: §e" + user.getAllParent(uuid)));
            return;
        }

        if (args.length == 4 && (args[3].equals("add") || args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired"))) {
            syntax(source);
            return;
        }

        String parent;
        switch (args[3]) {
            case "add":
                parent = args[4];
                if (args.length == 5) {
                    user.addParent(uuid, username, parent);
                    source.sendMessage(Component.text("§3Parent added §e" + parent));
                    break;
                }
                user.addTempParent(uuid, username, parent, formatTime(source, args));
                source.sendMessage(Component.text("§3Parent added §e" + parent));
                break;
            case "remove":
                parent = args[4];
                user.removeParent(uuid, parent);
                user.removeTempParent(uuid, parent);
                source.sendMessage(Component.text("§3Parent removed §e" + parent));
                break;
            case "clear":
                user.clearParent(uuid);
                user.clearTempParent(uuid);
                source.sendMessage(Component.text("§3Parent cleared"));
                break;
            case "created":
                parent = args[4];
                source.sendMessage(Component.text("§3Created: §e" + user.getTempParentCreated(uuid, parent)));
                break;
            case "expired":
                parent = args[4];
                source.sendMessage(Component.text("§3Expired: §e" + user.getTempParentExpired(uuid, parent)));
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void userPermission(CommandSource source, UUID uuid, String username, User user, String[] args) {
        if (args.length == 3) {
            source.sendMessage(Component.text("§3Permission: §e" + user.getAllPermission(uuid)));
            return;
        }

        if (args.length == 4 && (args[3].equals("add") || args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired"))) {
            syntax(source);
            return;
        }

        String permission;
        switch (args[3]) {
            case "all":
                source.sendMessage(Component.text("§3All permission: §e" + this.permission.getFinalPermission(uuid)));
                break;
            case "add":
                permission = args[4];
                if (args.length == 5) {
                    user.addPermission(uuid, username, permission);
                    source.sendMessage(Component.text("§3Permission added §e" + permission));
                    break;
                }
                user.addTempPermission(uuid, username, permission, formatTime(source, args));
                source.sendMessage(Component.text("§3Permission added §e" + permission));
                break;
            case "remove":
                permission = args[4];
                user.removePermission(uuid, permission);
                user.removeTempPermission(uuid, permission);
                source.sendMessage(Component.text("§3Permission removed §e" + permission));
                break;
            case "clear":
                user.clearPermission(uuid);
                user.clearTempPermission(uuid);
                source.sendMessage(Component.text("§3Permission cleared"));
                break;
            case "created":
                permission = args[4];
                source.sendMessage(Component.text("§3Created: §e" + user.getTempPermissionCreated(uuid, permission)));
                break;
            case "expired":
                permission = args[4];
                source.sendMessage(Component.text("§3Expired: §e" + user.getTempPermissionExpired(uuid, permission)));
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void groups(CommandSource source, Group group, String[] args) {

    }

    private long formatTime(CommandSource source, String[] args) {
        try {
            String format = args[5].substring(args[5].length() - 1);
            long duration = Long.parseLong(args[5].substring(0, args[5].length() - 1));
            long time = 0L;

            switch (format) {
                case "s":
                    time = duration * 1000L;
                    break;
                case "m":
                    time = duration * 1000L * 60L;
                    break;
                case "h":
                    time = duration * 1000L * 60L * 60L;
                    break;
                case "d":
                    time = duration * 1000L * 60L * 60L * 24L;
                    break;
                case "w":
                    time = duration * 1000L * 60L * 60L * 24L * 7L;
                    break;
                case "M":
                    time = duration * 1000L * 60L * 60L * 24L * 30L;
                    break;
                case "y":
                    time = duration * 1000L * 60L * 60L * 24L * 365L;
                    break;
                default:
                    source.sendMessage(Component.text("§cWrong valid format."));
                    break;
            }
            return time;
        } catch (NumberFormatException e) {
            source.sendMessage(Component.text("§cWrong valid format."));
        }
        return 0L;
    }

    private void syntax(CommandSource source) {
        source.sendMessage(Component.text("§8--- §cSyntax §8---§r"
                + "\n- /permission users <username> parent"
                + "\n- /permission users <username> parent add <parent> [duration]"
                + "\n- /permission users <username> parent remove <parent>"
                + "\n- /permission users <username> parent clear"
                + "\n- /permission users <username> parent created <parent>"
                + "\n- /permission users <username> parent expired <parent>"
                + "\n- /permission users <username> permission"
                + "\n- /permission users <username> permission all"
                + "\n- /permission users <username> permission add <permission> [duration]"
                + "\n- /permission users <username> permission remove <permission>"
                + "\n- /permission users <username> permission clear"
                + "\n- /permission users <username> permission created <permission>"
                + "\n- /permission users <username> permission expired <permission>"
        ));
    }
}
