package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
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

	private GameState gameState;

	private GameManager gameManager;

	private CageManager cageManager;

	public void CageScheduler()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.timer = config.getInt("schedulers.cages.timer");
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
		this.cageManager = VylariaUHCRun.getInstance().getCageManager();
	}

	@Override
	public void run()
	{
		timer--;

		if (timer == 0)
		{
			stop();

			gameState.setState(GameState.GAME);
			
			cageManager.remove(false);

			for (Player players : Bukkit.getOnlinePlayers())
			{
				players.setLevel(0);
				
				gameManager.removePreviousLocation(players);
			}

			gameManager.gameSetup();

			new TimeScheduler().runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20);
			
			InvincibilityScheduler invincibilityScheduler = new InvincibilityScheduler();
			invincibilityScheduler.setRunning(true);
			invincibilityScheduler.runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20L);
			
			PvPScheduler pvpScheduler = new PvPScheduler();
			pvpScheduler.setRunning(true);
			pvpScheduler.runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20L);
			
			new BorderTimeScheduler().runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20L);
			
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
