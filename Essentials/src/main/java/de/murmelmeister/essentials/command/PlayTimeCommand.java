package de.murmelmeister.essentials.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.murmelmeister.murmelapi.playtime.PlayTime;
import de.murmelmeister.murmelapi.playtime.PlayTimeUtil;
import de.murmelmeister.murmelapi.util.StringUtils;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayTimeCommand implements SimpleCommand {
    private final PlayTime playTime;

    public PlayTimeCommand(PlayTime playTime) {
        this.playTime = playTime;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource sender = invocation.source();
        if (args.length == 0) {
            Player player = sender instanceof Player ? (Player) sender : null;
            if (player == null) {
                sender.sendMessage(Component.text("§cThe command can not use in the proxy console."));
                return;
            }
            String time = PlayTimeUtil.format(playTime, player.getUniqueId());
            player.sendMessage(Component.text("§3PlayTime: §e" + time));
        } else if (args.length == 1) {
            String target = args[0];
            if (!(playTime.hasUUID(target))) {
                sender.sendMessage(Component.text(String.format("§cThe player §e%s§c does not exist.", target)));
                return;
            }
            UUID uuid = playTime.uuid(target);
            String time = PlayTimeUtil.format(playTime, uuid);
            sender.sendMessage(Component.text(String.format("§3PlayTime from §e%s§3: §e%s", target, time)));
        } else sender.sendMessage(Component.text("§7Syntax: §c/playtime [player]"));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (invocation.arguments().length == 1)
            return playTime.usernames().stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        return Collections.emptyList();
    }
}
