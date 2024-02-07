package de.murmelmeister.essentials.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.murmelmeister.murmelapi.permission.Group;
import de.murmelmeister.murmelapi.permission.Permission;
import de.murmelmeister.murmelapi.permission.PermissionConfig;
import de.murmelmeister.murmelapi.permission.User;
import de.murmelmeister.murmelapi.util.StringUtils;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        if (args.length == 1) {
            if (args[0].equals("grouplist")) {
                source.sendMessage(Component.text("§3Groups: "));
                for (String name : group.groups())
                    source.sendMessage(Component.text("§r- §e" + name));
            } else syntax(source);
            return;
        }

        if (args.length == 2) {
            if (args[0].equals("config")) {
                String id = args[1];
                id = id.toUpperCase();
                try {
                    PermissionConfig config = PermissionConfig.valueOf(id);
                    source.sendMessage(Component.text("§3Config: "));
                    source.sendMessage(Component.text("§r- §bID: §e" + config.getId()));
                    source.sendMessage(Component.text("§r- §bValue: §e" + permission.getValueString(config)));
                } catch (IllegalArgumentException e) {
                    source.sendMessage(Component.text("§cUse a valid ID."));
                }
            } else syntax(source);
            return;
        }

        if (args.length >= 3) {
            switch (args[0]) {
                case "users":
                    users(source, user, args);
                    break;
                case "groups":
                    groups(source, group, user, args);
                    break;
                case "config":
                    setConfig(source, args);
                    break;
                default:
                    syntax(source);
                    break;
            }
        } else syntax(source);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        Group group = permission.getGroup();
        User user = permission.getUser();
        if (args.length == 1)
            return Stream.of("users", "groups", "grouplist", "config").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 2 && args[0].equals("users"))
            return user.usernames().stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 2 && args[0].equals("groups"))
            return group.groups().stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 3 && args[0].equals("users"))
            return Stream.of("parent", "permission").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 4 && args[0].equals("users") && args[2].equals("parent"))
            return Stream.of("add", "remove", "clear", "created", "expired").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 4 && args[0].equals("users") && args[2].equals("permission"))
            return Stream.of("all", "add", "remove", "clear", "created", "expired").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 5 && args[0].equals("users") && args[2].equals("permission") && (args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired")))
            return user.getPermissions(user.uuid(args[1])).stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 5 && args[0].equals("users") && args[2].equals("parent") && (args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired")))
            return user.getParents(user.uuid(args[1])).stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 5 && args[0].equals("users") && args[2].equals("parent") && args[3].equals("add"))
            return group.groups().stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 3 && args[0].equals("groups"))
            return Stream.of("create", "delete", "rename", "edit", "parent", "permission").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 4 && args[0].equals("groups") && args[2].equals("parent"))
            return Stream.of("add", "remove", "clear", "created", "expired").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 4 && args[0].equals("groups") && args[2].equals("permission"))
            return Stream.of("all", "add", "remove", "clear", "created", "expired").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 4 && args[0].equals("groups") && args[2].equals("edit"))
            return Stream.of("chatprefix", "chatsuffix", "chatcolor", "groupprefix", "groupsuffix", "groupcolor", "tabprefix",
                    "tabsuffix", "tabcolor", "tabid", "teamid", "scoreboardprefix", "scoreboardcolor").filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 5 && args[0].equals("groups") && args[2].equals("permission") && (args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired")))
            return group.getPermissions(args[1]).stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 5 && args[0].equals("groups") && args[2].equals("parent") && (args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired")))
            return group.getParents(args[1]).stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 5 && args[0].equals("groups") && args[2].equals("parent") && args[3].equals("add"))
            return group.groups().stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        if (args.length == 2 && args[0].equals("config")) {
            List<String> ids = new ArrayList<>();
            for (PermissionConfig value : PermissionConfig.values()) ids.add(value.getId());
            return ids.stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void setConfig(CommandSource source, String[] args) {
        String id = args[1];
        id = id.toUpperCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < args.length; i++)
            builder.append(args[i]).append(" ");
        String message = builder.toString();
        message = message.trim();
        message = message.replace("\"", "").replace("'", "");
        try {
            PermissionConfig config = PermissionConfig.valueOf(id);
            config.setValue(message);
            permission.setConfigValue(config);
            source.sendMessage(Component.text("§3Config: "));
            source.sendMessage(Component.text("§r- §bID: §e" + config.getId()));
            source.sendMessage(Component.text("§r- §bNew value: §e" + permission.getValueString(config)));
        } catch (IllegalArgumentException e) {
            source.sendMessage(Component.text("§cUse a valid ID."));
        }
    }

    private void users(CommandSource source, User user, String[] args) {
        String username = args[1];
        if (!(user.hasUUID(username))) {
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
            source.sendMessage(Component.text("§3Parents: "));
            for (String all : user.getParents(uuid))
                source.sendMessage(Component.text("§r- §e" + all));
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
                    user.addParent(uuid, username, parent, -1);
                    source.sendMessage(Component.text("§3Parent added §e" + parent));
                    break;
                }
                user.addParent(uuid, username, parent, formatTime(source, args));
                source.sendMessage(Component.text("§3Parent added §e" + parent));
                break;
            case "remove":
                parent = args[4];
                if (parent.equals(permission.defaultGroup())) {
                    source.sendMessage(Component.text("§cYou can not remove the default group."));
                    break;
                }
                user.removeParent(uuid, parent);
                source.sendMessage(Component.text("§3Parent removed §e" + parent));
                break;
            case "clear":
                user.clearParent(uuid);
                user.addParent(uuid, username, permission.defaultGroup(), -1);
                source.sendMessage(Component.text("§3Parent cleared"));
                break;
            case "created":
                parent = args[4];
                source.sendMessage(Component.text("§3Created: §e" + user.getParentCreated(uuid, parent)));
                break;
            case "expired":
                parent = args[4];
                source.sendMessage(Component.text("§3Expired: §e" + user.getParentExpired(uuid, parent)));
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void userPermission(CommandSource source, UUID uuid, String username, User user, String[] args) {
        if (args.length == 3) {
            source.sendMessage(Component.text("§3Permission: "));
            for (String all : user.getPermissions(uuid))
                source.sendMessage(Component.text("§r- §e" + all));
            return;
        }

        if (args.length == 4 && (args[3].equals("add") || args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired"))) {
            syntax(source);
            return;
        }

        String permission;
        switch (args[3]) {
            case "all":
                source.sendMessage(Component.text("§3All permission: "));
                for (String all : this.permission.getFinalPermissions(uuid))
                    source.sendMessage(Component.text("§r- §e" + all));
                break;
            case "add":
                permission = args[4];
                if (args.length == 5) {
                    user.addPermission(uuid, username, permission, -1);
                    source.sendMessage(Component.text("§3Permission added §e" + permission));
                    break;
                }
                user.addPermission(uuid, username, permission, formatTime(source, args));
                source.sendMessage(Component.text("§3Permission added §e" + permission));
                break;
            case "remove":
                permission = args[4];
                user.removePermission(uuid, permission);
                source.sendMessage(Component.text("§3Permission removed §e" + permission));
                break;
            case "clear":
                user.clearPermission(uuid);
                source.sendMessage(Component.text("§3Permission cleared"));
                break;
            case "created":
                permission = args[4];
                source.sendMessage(Component.text("§3Created: §e" + user.getPermissionCreated(uuid, permission)));
                break;
            case "expired":
                permission = args[4];
                source.sendMessage(Component.text("§3Expired: §e" + user.getPermissionExpired(uuid, permission)));
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void groups(CommandSource source, Group group, User user, String[] args) {
        String name = args[1];
        if (!(group.exists(name))) {
            if (args[2].equals("create")) {
                group.create(name);
                group.addPermission(name, permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + name, -1);
                source.sendMessage(Component.text(String.format("§aThe Group §e%s§a was created.", name)));
            } else {
                source.sendMessage(Component.text(String.format("§cThe Group §e%s§c does not exist.", name)));
            }
            return;
        }

        switch (args[2]) {
            case "delete":
                for (UUID uuid : user.uuids())
                    if (user.containsParent(uuid, name)) user.removeParent(uuid, name);
                group.delete(name);
                source.sendMessage(Component.text(String.format("§aThe Group §e%s§a was deleted.", name)));
                break;
            case "rename":
                String newName = args[3];
                group.rename(name, newName);
                source.sendMessage(Component.text(String.format("§aThe Group §e%s§a was changed name to §e%s§a.", name, newName)));
                break;
            case "edit":
                groupEdit(source, name, group, args);
                break;
            case "parent":
                groupParent(source, name, group, args);
                break;
            case "permission":
                groupPermission(source, name, group, args);
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void groupEdit(CommandSource source, String name, Group group, String[] args) {
        if (args.length == 3) {
            syntax(source);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 4; i < args.length; i++)
            builder.append(args[i]).append(" ");
        String message = builder.toString();
        message = message.trim();
        message = message.replace("\"", "").replace("'", "");

        switch (args[3]) {
            case "chatprefix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3ChatPrefix: §e" + group.chatPrefix(name)));
                    break;
                }
                group.chatPrefix(name, message);
                source.sendMessage(Component.text("§3New ChatPrefix: §e" + message));
                break;
            case "chatsuffix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3ChatSuffix: §e" + group.chatSuffix(name)));
                    break;
                }
                group.chatSuffix(name, message);
                source.sendMessage(Component.text("§3New ChatSuffix: §e" + message));
                break;
            case "chatcolor":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3ChatColor: §e" + group.chatColor(name)));
                    break;
                }
                group.chatColor(name, message);
                source.sendMessage(Component.text("§3New ChatColor: §e" + message));
                break;
            case "groupprefix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3GroupPrefix: §e" + group.groupPrefix(name)));
                    break;
                }
                group.groupPrefix(name, message);
                source.sendMessage(Component.text("§3New GroupPrefix: §e" + message));
                break;
            case "groupsuffix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3GroupSuffix: §e" + group.groupSuffix(name)));
                    break;
                }
                group.groupSuffix(name, message);
                source.sendMessage(Component.text("§3New GroupSuffix: §e" + message));
                break;
            case "groupcolor":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3GroupColor: §e" + group.groupColor(name)));
                    break;
                }
                group.groupColor(name, message);
                source.sendMessage(Component.text("§3New GroupColor: §e" + message));
                break;
            case "tabprefix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3TabPrefix: §e" + group.tabPrefix(name)));
                    break;
                }
                group.tabPrefix(name, message);
                source.sendMessage(Component.text("§3New TabPrefix: §e" + message));
                break;
            case "tabsuffix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3TabSuffix: §e" + group.tabSuffix(name)));
                    break;
                }
                group.tabSuffix(name, message);
                source.sendMessage(Component.text("§3New TabSuffix: §e" + message));
                break;
            case "tabcolor":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3TabColor: §e" + group.tabColor(name)));
                    break;
                }
                group.tabColor(name, message);
                source.sendMessage(Component.text("§3New TabColor: §e" + message));
                break;
            case "tabid":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3TabID: §e" + group.tabID(name)));
                    break;
                }
                group.tabID(name, message);
                group.teamID(name, message + name);
                source.sendMessage(Component.text("§3New TabID: §e" + message));
                break;
            case "teamid":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3TeamID: §e" + group.teamID(name)));
                    break;
                }
                group.teamID(name, message);
                source.sendMessage(Component.text("§3New TeamID: §e" + message));
                break;
            case "scoreboardprefix":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3ScoreboardPrefix: §e" + group.scoreboardPrefix(name)));
                    break;
                }
                group.scoreboardPrefix(name, message);
                source.sendMessage(Component.text("§3New ScoreboardPrefix: §e" + message));
                break;
            case "scoreboardcolor":
                if (args.length == 4) {
                    source.sendMessage(Component.text("§3ScoreboardColor: §e" + group.scoreboardColor(name)));
                    break;
                }
                group.scoreboardColor(name, message);
                source.sendMessage(Component.text("§3New ScoreboardColor: §e" + message));
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void groupParent(CommandSource source, String name, Group group, String[] args) {
        if (args.length == 3) {
            source.sendMessage(Component.text("§3Parents: "));
            for (String all : group.getParents(name))
                source.sendMessage(Component.text("§r- §e" + all));
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
                    group.addParent(name, parent, -1);
                    source.sendMessage(Component.text("§3Parent added §e" + parent));
                    break;
                }
                group.addParent(name, parent, formatTime(source, args));
                source.sendMessage(Component.text("§3Parent added §e" + parent));
                break;
            case "remove":
                parent = args[4];
                group.removeParent(name, parent);
                source.sendMessage(Component.text("§3Parent removed §e" + parent));
                break;
            case "clear":
                group.clearParent(name);
                source.sendMessage(Component.text("§3Parent cleared"));
                break;
            case "created":
                parent = args[4];
                source.sendMessage(Component.text("§3Created: §e" + group.getParentCreated(name, parent)));
                break;
            case "expired":
                parent = args[4];
                source.sendMessage(Component.text("§3Expired: §e" + group.getParentExpired(name, parent)));
                break;
            default:
                syntax(source);
                break;
        }
    }

    private void groupPermission(CommandSource source, String name, Group group, String[] args) {
        if (args.length == 3) {
            source.sendMessage(Component.text("§3Permission: "));
            for (String all : group.getPermissions(name))
                source.sendMessage(Component.text("§r- §e" + all));
            return;
        }

        if (args.length == 4 && (args[3].equals("add") || args[3].equals("remove") || args[3].equals("created") || args[3].equals("expired"))) {
            syntax(source);
            return;
        }

        String permission;
        switch (args[3]) {
            case "all":
                source.sendMessage(Component.text("§3All permission: "));
                for (String all : group.getAllPermissions(name))
                    source.sendMessage(Component.text("§r- §e" + all));
                break;
            case "add":
                permission = args[4];
                if (args.length == 5) {
                    group.addPermission(name, permission, -1);
                    source.sendMessage(Component.text("§3Permission added §e" + permission));
                    break;
                }
                group.addPermission(name, permission, formatTime(source, args));
                source.sendMessage(Component.text("§3Permission added §e" + permission));
                break;
            case "remove":
                permission = args[4];
                if (permission.equals(this.permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + name)) {
                    source.sendMessage(Component.text("§cThe default permission can not be removed."));
                    break;
                }
                group.removePermission(name, permission);
                source.sendMessage(Component.text("§3Permission removed §e" + permission));
                break;
            case "clear":
                group.clearPermission(name);
                group.addPermission(name, this.permission.getValueString(PermissionConfig.DEFAULT_PRE_PERMISSION) + name, -1);
                source.sendMessage(Component.text("§3Permission cleared"));
                break;
            case "created":
                permission = args[4];
                source.sendMessage(Component.text("§3Created: §e" + group.getPermissionCreated(name, permission)));
                break;
            case "expired":
                permission = args[4];
                source.sendMessage(Component.text("§3Expired: §e" + group.getPermissionExpired(name, permission)));
                break;
            default:
                syntax(source);
                break;
        }
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
                + "\n- /permission §9users§r <username> parent"
                + "\n- /permission §9users§r <username> parent add <parent> [duration]"
                + "\n- /permission §9users§r <username> parent remove <parent>"
                + "\n- /permission §9users§r <username> parent clear"
                + "\n- /permission §9users§r <username> parent created <parent>"
                + "\n- /permission §9users§r <username> parent expired <parent>"
                + "\n- /permission §9users§r <username> permission"
                + "\n- /permission §9users§r <username> permission all"
                + "\n- /permission §9users§r <username> permission add <permission> [duration]"
                + "\n- /permission §9users§r <username> permission remove <permission>"
                + "\n- /permission §9users§r <username> permission clear"
                + "\n- /permission §9users§r <username> permission created <permission>"
                + "\n- /permission §9users§r <username> permission expired <permission>"
                + "\n- /permission §3groups§r <name> create"
                + "\n- /permission §3groups§r <name> delete"
                + "\n- /permission §3groups§r <name> rename <newName>"
                + "\n- /permission §3groups§r <name> edit chatprefix [prefix]"
                + "\n- /permission §3groups§r <name> edit chatsuffix [suffix]"
                + "\n- /permission §3groups§r <name> edit chatcolor [color]"
                + "\n- /permission §3groups§r <name> edit groupprefix [prefix]"
                + "\n- /permission §3groups§r <name> edit groupsuffix [suffix]"
                + "\n- /permission §3groups§r <name> edit groupcolor [color]"
                + "\n- /permission §3groups§r <name> edit tabprefix [prefix]"
                + "\n- /permission §3groups§r <name> edit tabsuffix [suffix]"
                + "\n- /permission §3groups§r <name> edit tabcolor [color]"
                + "\n- /permission §3groups§r <name> edit tabid [id]"
                + "\n- /permission §3groups§r <name> edit teamid [id]"
                + "\n- /permission §3groups§r <name> edit scoreboardprefix [prefix]"
                + "\n- /permission §3groups§r <name> edit scoreboardcolor [color]"
                + "\n- /permission §3groups§r <name> parent"
                + "\n- /permission §3groups§r <name> parent add <parent> [duration]"
                + "\n- /permission §3groups§r <name> parent remove <parent>"
                + "\n- /permission §3groups§r <name> parent clear"
                + "\n- /permission §3groups§r <name> parent created <parent>"
                + "\n- /permission §3groups§r <name> parent expired <parent>"
                + "\n- /permission §3groups§r <name> permission"
                + "\n- /permission §3groups§r <name> permission all"
                + "\n- /permission §3groups§r <name> permission add <permission> [duration]"
                + "\n- /permission §3groups§r <name> permission remove <permission>"
                + "\n- /permission §3groups§r <name> permission clear"
                + "\n- /permission §3groups§r <name> permission created <permission>"
                + "\n- /permission §3groups§r <name> permission expired <permission>"
                + "\n- /permission §dgrouplist§r"
                + "\n- /permission §bconfig§r <id> <value>"
        ));
    }
}
