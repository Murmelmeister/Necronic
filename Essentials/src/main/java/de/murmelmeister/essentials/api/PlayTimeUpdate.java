package de.murmelmeister.essentials.api;

import com.velocitypowered.api.proxy.ProxyServer;
import de.murmelmeister.murmelapi.playtime.PlayTime;

import java.util.UUID;

public class PlayTimeUpdate {
    public static void setPlayTimeTimer(PlayTime playTime, UUID uuid) {
        playTime.addSeconds(uuid);
        if (playTime.getSeconds(uuid) >= 60) {
            playTime.resetSeconds(uuid);
            playTime.addMinutes(uuid);
        } else if (playTime.getMinutes(uuid) >= 60) {
            playTime.resetMinutes(uuid);
            playTime.addHours(uuid);
        } else if (playTime.getHours(uuid) >= 24) {
            playTime.resetHours(uuid);
            playTime.addDays(uuid);
        } else if (playTime.getDays(uuid) >= 365) {
            playTime.resetDays(uuid);
            playTime.addYears(uuid);
        }
    }

    public static void startTimer(ProxyServer proxyServer, PlayTime playTime) {
        proxyServer.getAllPlayers().forEach(player -> setPlayTimeTimer(playTime, player.getUniqueId()));
    }
}
