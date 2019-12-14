package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.BorderManager;
import fr.geeklegend.vylaria.uhcrun.game.CageManager;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartScheduler extends BukkitRunnable implements IScheduler
{

	private FileConfiguration config;

	private int timer;

	private boolean running;

	private GameState gameState;

	private BorderManager borderManager;
	private CageManager cageManager;
	private GameManager gameManager;

	public StartScheduler()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.timer = config.getInt("schedulers.start.timer");
		this.running = false;
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.borderManager = VylariaUHCRun.getInstance().getBorderManager();
		this.cageManager = VylariaUHCRun.getInstance().getCageManager();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
	}

	@Override
	public void run()
	{
		timer--;

		if (timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1)
		{
			for (Player players : Bukkit.getOnlinePlayers())
			{
				players.playSound(players.getLocation(), Sound.NOTE_PLING, 20.0f, 20.0f);
			}

			Bukkit.broadcastMessage(config.getString("messages.schedulers.start.while")
					.replace("&", "§").replace("%timer%", "" + timer));
		} else if (timer == 0)
		{
			stop();

			gameState.setState(GameState.PREGAME);

			borderManager.load();

			cageManager.remove(true);

			new CageScheduler().runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20L);

			for (Player players : Bukkit.getOnlinePlayers())
			{
				gameManager.removePreviousLocation(players);
			}

			gameManager.preGameSetup();
		}

		for (Player players : Bukkit.getOnlinePlayers())
		{
			players.setLevel(timer);
		}

		if (Bukkit.getOnlinePlayers().size() < config.getInt("schedulers.start.minplayers"))
		{
			stop();

			gameState.setState(GameState.WAITING);

			Bukkit.broadcastMessage(
					config.getString("messages.schedulers.start.noenought").replace("&", "§"));
		}
	}

	@Override
	public void stop()
	{
		cancel();
		reset();
		running = false;
	}

	@Override
	public void reset()
	{
		timer = config.getInt("schedulers.start.timer");
	}

	public int getTimer()
	{
		return timer;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

}
