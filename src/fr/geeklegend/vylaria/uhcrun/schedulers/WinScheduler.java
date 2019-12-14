package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.WinManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WinScheduler extends BukkitRunnable
{

	private Player player;

	private int timer ;

	private boolean running;

	private WinManager winManager;

	public WinScheduler()
	{
		this.player = null;
		this.timer = 1;
		this.running = false;
	}

	@Override
	public void run()
	{
		timer--;

		if (timer == 0)
		{
			stop();

			winManager = new WinManager(player);
			winManager.check();
		}
	}

	public void stop()
	{
		cancel();
		running = false;
		timer = 1;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public int getTimer()
	{
		return timer;
	}

	public void setTimer(int timer)
	{
		this.timer = timer;
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
