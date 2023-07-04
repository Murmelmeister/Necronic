package de.murmelmeister.citybuild.util.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TestScoreboard extends ScoreboardBuilder {
    public TestScoreboard(Player player, Component displayName) {
        super(player, displayName);
        update();
    }

    @Override
    public void createScoreboard() {

    }

    @Override
    public void update() {

    }
}
