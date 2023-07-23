package de.murmelmeister.essentials.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.murmelmeister.essentials.Essentials;
import de.murmelmeister.essentials.api.PlayTimeUpdate;
import de.murmelmeister.murmelapi.playtime.PlayTime;

import java.util.concurrent.TimeUnit;

public class PlayTimeListener {
    private final PlayTime playTime;

    public PlayTimeListener(Essentials instance, ProxyServer proxyServer, PlayTime playTime) {
        this.playTime = playTime;
        proxyServer.getScheduler().buildTask(instance, () -> PlayTimeUpdate.startTimer(proxyServer, playTime)).repeat(1L, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void connect(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        playTime.createAccount(player.getUniqueId(), player.getUsername());
        playTime.renameUsername(player.getUniqueId(), player.getUsername());
    }
}
