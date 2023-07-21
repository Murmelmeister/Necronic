package de.murmelmeister.lobby.command.commands;

import de.murmelmeister.lobby.Main;
import de.murmelmeister.lobby.command.CommandManager;
import de.murmelmeister.lobby.util.config.Configs;
import de.murmelmeister.lobby.util.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RankCommand extends CommandManager {
    public RankCommand(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_COMMAND))) return true;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_COMMAND))) return true;

        if (args.length >= 2) {
            switch (args[0]) {
                case "get":
                    getRank(sender, args);
                    break;
                case "remove":
                    removeRank(sender, args);
                    break;
                case "add":
                    addRank(sender, args);
                    break;
                case "set":
                    setRank(sender, command, args);
                    break;
                default:
                    sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                    break;
            }
        } else sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return tabComplete(Arrays.asList("get", "remove", "add", "set"), args, 1);
        if (args.length == 2)
            return tabComplete(ranks.getRankList(), args, 2);
        if (args.length == 3 && args[0].equals("set"))
            return tabComplete(Arrays.asList("chatprefix", "chatsuffix", "chatcolor", "tabprefix", "tabsuffix", "tabcolor", "tabid", "teamid", "permission"), args, 3);
        return Collections.emptyList();
    }

    /*
    /rank get <name>
     */
    private void getRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_GET))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_GET))) return;

        String name = args[1];
        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        sendMessage(sender, ranks.getRank(name));
    }

    /*
    /rank remove <name>
     */
    private void removeRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_REMOVE))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_REMOVE))) return;

        String name = args[1];
        ranks.removeRank(name);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_REMOVE).replace("[NAME]", name));
    }

    /*
    /rank add <name> <chatPrefix> <chatSuffix> <chatColor> <tabPrefix> <tabSuffix> <tabColor> <tabID>
     */
    private void addRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_ADD))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_ADD))) return;

        if (args.length <= 8) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_USAGE));
            return;
        }

        String name = args[1];
        String chatPrefix = args[2].replace("null", "");
        String chatSuffix = args[3].replace("null", "");
        String chatColor = args[4].replace("null", "");
        String tabPrefix = args[5].replace("null", "");
        String tabSuffix = args[6].replace("null", "");
        String tabColor = args[7].replace("null", "");
        String tabID = args[8].replace("null", "");

        if (ranks.existRank(name)) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.addRank(name, chatPrefix, chatSuffix, chatColor, tabPrefix, tabSuffix, tabColor, tabID);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_ADD).replace("[NAME]", name));
    }

    /*
    /rank set <name> chatprefix <prefix>
    /rank set <name> chatsuffix <suffix>
    /rank set <name> chatcolor <color>
    /rank set <name> tabprefix <prefix>
    /rank set <name> tabsuffix <suffix>
    /rank set <name> tabcolor <color>
    /rank set <name> tabid <id>
    /rank set <name> teamid <id>
    /rank set <name> permission <permission>
     */
    private void setRank(CommandSender sender, Command command, String[] args) {
        if (args.length <= 3) {
            sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
            return;
        }

        switch (args[2]) {
            case "chatprefix":
                setChatPrefixRank(sender, args);
                break;
            case "chatsuffix":
                setChatSuffixRank(sender, args);
                break;
            case "chatcolor":
                setChatColorRank(sender, args);
                break;
            case "tabprefix":
                setTabPrefixRank(sender, args);
                break;
            case "tabsuffix":
                setTabSuffixRank(sender, args);
                break;
            case "tabcolor":
                setTabColorRank(sender, args);
                break;
            case "tabid":
                setTabIDRank(sender, args);
                break;
            case "teamid":
                setTeamIDRank(sender, args);
                break;
            case "permission":
                setPermissionRank(sender, args);
                break;
            default:
                sendMessage(sender, message.getString(Messages.COMMAND_SYNTAX).replace("[USAGE]", command.getUsage()));
                break;
        }
    }

    /*
    /rank set <name> chatprefix <prefix>
     */
    private void setChatPrefixRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_CHAT_PREFIX))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_CHAT_PREFIX))) return;

        String name = args[1];
        String prefix = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setChatPrefix(name, prefix);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> chatsuffix <suffix>
     */
    private void setChatSuffixRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_CHAT_SUFFIX))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_CHAT_SUFFIX))) return;

        String name = args[1];
        String suffix = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setChatSuffix(name, suffix);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> chatcolor <color>
     */
    private void setChatColorRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_CHAT_COLOR))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_CHAT_COLOR))) return;


        String name = args[1];
        String color = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setChatColor(name, color);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> tabprefix <prefix>
     */
    private void setTabPrefixRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_TAB_PREFIX))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_TAB_PREFIX))) return;

        String name = args[1];
        String prefix = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setTabPrefix(name, prefix);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> tabsuffix <suffix>
     */
    private void setTabSuffixRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_TAB_SUFFIX))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_TAB_SUFFIX))) return;

        String name = args[1];
        String suffix = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setTabSuffix(name, suffix);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> tabcolor <color>
     */
    private void setTabColorRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_TAB_COLOR))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_TAB_COLOR))) return;

        String name = args[1];
        String color = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setTabColor(name, color);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> tabid <id>
     */
    private void setTabIDRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_TAB_ID))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_TAB_ID))) return;

        String name = args[1];
        String id = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setTabID(name, id);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> teamid <id>
     */
    private void setTeamIDRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_TEAM_ID))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_TEAM_ID))) return;

        String name = args[1];
        String id = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setTeamID(name, id);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }

    /*
    /rank set <name> permission <permission>
     */
    private void setPermissionRank(CommandSender sender, String[] args) {
        if (!(isEnable(sender, Configs.COMMAND_ENABLE_RANK_SET_PERMISSION))) return;
        if (!(hasPermission(sender, Configs.PERMISSION_RANK_SET_PERMISSION))) return;

        String name = args[1];
        String permission = args[3];

        if (!(ranks.existRank(name))) {
            sendMessage(sender, message.getString(Messages.COMMAND_RANK_NOT_EXIST).replace("[NAME]", name));
            return;
        }

        ranks.setPermission(name, permission);
        sendMessage(sender, message.getString(Messages.COMMAND_RANK_SET).replace("[NAME]", name));
    }
}
