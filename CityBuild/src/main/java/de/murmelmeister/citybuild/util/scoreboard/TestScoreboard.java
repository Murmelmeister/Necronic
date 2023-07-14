package de.murmelmeister.citybuild.util.scoreboard;

import de.murmelmeister.citybuild.CityBuild;
import de.murmelmeister.citybuild.Main;
import de.murmelmeister.citybuild.api.Economy;
import de.murmelmeister.citybuild.api.Ranks;
import de.murmelmeister.citybuild.api.SchedulerTask;
import de.murmelmeister.citybuild.configs.Config;
import de.murmelmeister.citybuild.configs.Message;
import de.murmelmeister.citybuild.util.HexColor;
import de.murmelmeister.citybuild.util.config.Configs;
import de.murmelmeister.citybuild.util.config.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class TestScoreboard extends ScoreboardBuilder {
    private CityBuild instance;
    private Server server;
    private Config config;
    private final SchedulerTask schedulerTask;

    public TestScoreboard(Player player, Main main) {
        super(player, Component.text("Dummy"), main);
        this.schedulerTask = main.getSchedulerTask();
        update();
    }

    @Override
    public void createScoreboard() {
        setScoreboard();
    }

    @Override
    public void update() {
        schedulerTask.addBukkitTask(player,
                server.getScheduler().runTaskTimerAsynchronously(instance, () -> setScoreboard(), 2L, config.getLong(Configs.SCOREBOARD_UPDATE_SCORE_TIMER) * 20L));
    }

    private void setScoreboard() {
        this.instance = main.getInstance();
        this.server = main.getInstance().getServer();
        this.config = main.getConfig();
        Message message = main.getMessage();
        Ranks ranks = main.getRanks();
        Economy economy = main.getEconomy();
        DecimalFormat decimalFormat = new DecimalFormat(config.getString(Configs.PATTERN_DECIMAL));
        setDisplayName(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_DISPLAY_NAME)));
        ranks.getRankList().forEach(s -> {
            if (player.hasPermission(ranks.getPermission(s))) {
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_15))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_15)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 15);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_14))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_14)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 14);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_13))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_13)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 13);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_12))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_12)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 12);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_11))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_11)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 11);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_10))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_10)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 10);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_9))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_9)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 9);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_8))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_8)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 8);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_7))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_7)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 7);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_6))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_6)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 6);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_5))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_5)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 5);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_4))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_4)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 4);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_3))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_3)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 3);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_2))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_2)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 2);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_1))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_1)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 1);
                if (config.getBoolean(Configs.SCOREBOARD_ENABLE_SCORE_0))
                    setScoreTeam(HexColor.format(message.getString(Messages.SCOREBOARD_SCORE_0)
                            .replace("[RANK_COLOR]", ranks.getTabColor(s)).replace("[RANK]", ranks.getScoreboard(s)).replace("[SERVER]", config.getString(Configs.CURRENT_SERVER))
                            .replace("[CURRENCY]", config.getString(Configs.ECONOMY_CURRENCY)).replace("[MONEY]", decimalFormat.format(economy.getMoney(player.getUniqueId())))), 0);
            }
        });
    }
}
