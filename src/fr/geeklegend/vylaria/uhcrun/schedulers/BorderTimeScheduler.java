package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.BorderManager;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderTimeScheduler extends BukkitRunnable implements IScheduler
{

	private FileConfiguration config;
	
	private int timer;

	private boolean running;

	private GameManager gameManager;
	private BorderManager borderManager;

	public void BorderTimeScheduler()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.timer = config.getInt("schedulers.border.timer");
		this.running = false;
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
		this.borderManager = VylariaUHCRun.getInstance().getBorderManager();
	}

	@Override
	public void run()
	{
		timer--;

		if (timer == 0)
		{
			stop();
			
			gameManager.setBorder(true);
			borderManager.setMoved(true);

			new BorderScheduler().runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20L);
			
			Bukkit.broadcastMessage(config.getString("messages.border").replace("&", "§"));
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
		timer = config.getInt("schedulers.border.timer");
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
