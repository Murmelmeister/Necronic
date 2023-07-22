package de.murmelmeister.lobby.util.scoreboard;

import de.murmelmeister.lobby.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TestScoreboard extends ScoreboardBuilder {

    public TestScoreboard(Player player, Main main) {
        super(player, Component.text("Dummy"), main);
        update();
    }

    @Override
    public void createScoreboard() {
        setScoreboard();
    }

    @Override
    public void update() {
    }

    private void setScoreboard() {
    }
}
