package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.CageManager;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CageScheduler extends BukkitRunnable implements IScheduler
{

	private FileConfiguration config;

	private int timer;

	private GameManager gameManager;

	private CageManager cageManager;

	public CageScheduler()
	{
		this.config = UHCRun.getInstance().getConfig();
		this.timer = config.getInt("schedulers.cages.timer");
		this.gameManager = UHCRun.getInstance().getGameManager();
		this.cageManager = UHCRun.getInstance().getCageManager();
	}

	@Override
	public void run()
	{
		timer--;

		if (timer == 0)
		{
			stop();

			GameState.setState(GameState.GAME);
			
			cageManager.remove(false);

			for (Player players : Bukkit.getOnlinePlayers())
			{
				players.setLevel(0);
				
				gameManager.removePreviousLocation(players);
			}

			gameManager.gameSetup();

			new TimeScheduler().runTaskTimer(UHCRun.getInstance(), 20L, 20);
			
			InvincibilityScheduler invincibilityScheduler = new InvincibilityScheduler();
			invincibilityScheduler.setRunning(true);
			invincibilityScheduler.runTaskTimer(UHCRun.getInstance(), 20L, 20L);
			
			PvPScheduler pvpScheduler = new PvPScheduler();
			pvpScheduler.setRunning(true);
			pvpScheduler.runTaskTimer(UHCRun.getInstance(), 20L, 20L);
			
			new BorderTimeScheduler().runTaskTimer(UHCRun.getInstance(), 20L, 20L);
			
			Bukkit.broadcastMessage(config.getString("messages.invincibility.start").replace("&", "§")
					.replace("%timer%", "" + invincibilityScheduler.getTimer()));
		}
	}

	@Override
	public void stop()
	{
		cancel();
		reset();
	}

	@Override
	public void reset()
	{
		timer = config.getInt("schedulers.cages.timer");
	}

}
