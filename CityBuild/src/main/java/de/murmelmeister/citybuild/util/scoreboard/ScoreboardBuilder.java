package de.murmelmeister.citybuild.util.scoreboard;

import de.murmelmeister.citybuild.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public abstract class ScoreboardBuilder {
    protected final Scoreboard scoreboard;
    protected final Objective objective;

    protected final Player player;
    protected final Main main;

    public ScoreboardBuilder(Player player, Component displayName, Main main) {
        this.main = main;
        this.player = player;
        if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        this.scoreboard = player.getScoreboard();

        if (this.scoreboard.getObjective("display") != null)
            Objects.requireNonNull(this.scoreboard.getObjective("display")).unregister();

        this.objective = this.scoreboard.registerNewObjective("display", Criteria.DUMMY, displayName);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        createScoreboard();
    }

    public abstract void createScoreboard();

    public abstract void update();

    public void setDisplayName(Component displayName) {
        this.objective.displayName(displayName);
    }

    @Deprecated
    public void setDisplayName(String displayName) {
        this.objective.setDisplayName(displayName);
    }

    public void setScoreContent(String content, int score) {
        this.objective.getScore(content).setScore(score);
    }

    public void removeScoreContent(String content) {
        this.scoreboard.resetScores(content);
    }

    public void setScoreTeam(Component content, int score) {
        Team team = getTeamByScore(score);
        if (team == null) return;
        team.prefix(content);
        showScore(score);
    }

    @Deprecated
    public void setScoreTeam(String content, int score) {
        Team team = getTeamByScore(score);
        if (team == null) return;
        team.setPrefix(content);
        showScore(score);
    }

    public void removeScoreTeam(int score) {
        hideScore(score);
    }

    private EntryName getEntryNameByScore(int id) {
        for (EntryName name : EntryName.values())
            if (id == name.getEntry())
                return name;
        return null;
    }

    private Team getTeamByScore(int id) {
        EntryName name = getEntryNameByScore(id);
        if (name == null) return null;

        Team team = scoreboard.getEntryTeam(name.getEntryName());
        if (team != null) return team;

        team = scoreboard.registerNewTeam(name.name());
        team.addEntry(name.getEntryName());
        return team;
    }

    private void showScore(int score) {
        EntryName name = getEntryNameByScore(score);
        if (name == null) return;
        if (objective.getScore(name.getEntryName()).isScoreSet()) return;
        objective.getScore(name.getEntryName()).setScore(score);
    }

    private void hideScore(int score) {
        EntryName name = getEntryNameByScore(score);
        if (name == null) return;
        if (!objective.getScore(name.getEntryName()).isScoreSet()) return;
        scoreboard.resetScores(name.getEntryName());
    }
}
