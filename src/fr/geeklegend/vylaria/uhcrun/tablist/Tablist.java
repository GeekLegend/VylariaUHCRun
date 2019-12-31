package fr.geeklegend.vylaria.uhcrun.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Tablist
{

	private ScoreboardManager scoreboardManager;
	private Scoreboard scoreboard;
	private Objective objective;

	public Tablist()
	{
		this.scoreboardManager = Bukkit.getScoreboardManager();
		this.scoreboard = scoreboardManager.getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("health", "health");
	}

	public void setHealth()
	{
		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}

	public void setHealthScore(Player player)
	{
		Score score = objective.getScore(player);
		score.setScore((int) (player.getHealth() * 5));

		player.setScoreboard(scoreboard);
	}

	public ScoreboardManager getScoreboardManager()
	{
		return scoreboardManager;
	}

	public Scoreboard getScoreboard()
	{
		return scoreboard;
	}

	public Objective getObjective()
	{
		return objective;
	}

}
