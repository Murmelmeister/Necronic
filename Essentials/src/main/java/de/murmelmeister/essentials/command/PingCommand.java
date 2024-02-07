package de.murmelmeister.essentials.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.murmelmeister.murmelapi.util.StringUtils;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;

public class PingCommand implements SimpleCommand {
    private final ProxyServer proxyServer;

    public PingCommand(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        if (!source.hasPermission("essentials.command.ping")) {
            source.sendMessage(Component.text("§cYou don't have the right permission to do that."));
            return;
        }

        if (args.length == 0) {
            Player player = source instanceof Player ? (Player) source : null;
            if (player == null) {
                source.sendMessage(Component.text("§cThe command can not use in the proxy console."));
                return;
            }

            player.sendMessage(Component.text("§3Your ping: §e" + player.getPing() + " ms"));
        } else if (args.length == 1) {
            Optional<Player> target = proxyServer.getPlayer(args[0]);
            if (target.isEmpty()) {
                source.sendMessage(Component.text("§cThe player §e" + args[0] + "§c is not online."));
                return;
            }
            source.sendMessage(Component.text("§d" + target.get().getUsername() + "§3 ping: §e" + target.get().getPing() + " ms"));
        } else source.sendMessage(Component.text("§7Syntax: §c/ping [player]"));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> playerList = new ArrayList<>();
        for (Player player : proxyServer.getAllPlayers())
            playerList.add(player.getUsername());
        if (args.length == 1)
            return playerList.stream().filter(s -> StringUtils.startWithIgnoreCase(s, args[args.length - 1])).sorted().collect(Collectors.toList());
        return Collections.emptyList();
    }
}
