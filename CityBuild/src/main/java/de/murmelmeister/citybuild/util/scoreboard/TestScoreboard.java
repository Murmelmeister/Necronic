package de.murmelmeister.citybuild.util.scoreboard;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class TestScoreboard extends ScoreboardBuilder {
    private CityBuild instance;
    private Server server;
    private Config config;

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
        server.getScheduler().runTaskTimerAsynchronously(instance, () -> {
            setScoreboard();
        }, 2L, config.getLong(Configs.SCOREBOARD_UPDATE_SCORE_TIMER) * 20L);
    }

    private void setScoreboard() {
        this.instance = main.getInstance();
        this.server = main.getInstance().getServer();
        this.config = main.getConfig();
        Message message = main.getMessage();
        setDisplayName(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_DISPLAY_NAME)));
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_15))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_15)), 15);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_14))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_14)), 14);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_13))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_13)), 13);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_12))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_12)), 12);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_11))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_11)), 11);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_10))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_10)), 10);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_9))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_9)), 9);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_8))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_8)), 8);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_7))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_7)), 7);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_6))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_6)), 6);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_5))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_5)), 5);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_4))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_4)), 4);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_3))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_3)), 3);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_2))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_2)), 2);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_1))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_1)), 1);
        if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_0))
            setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_0)), 0);
    }
}
