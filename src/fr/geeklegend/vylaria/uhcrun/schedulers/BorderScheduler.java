package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.BorderManager;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderScheduler extends BukkitRunnable implements IScheduler
{
	
	private int timer;

	private BorderManager borderManager;

	public BorderScheduler()
	{
		this.timer = 0;
		this.borderManager = UHCRun.getInstance().getBorderManager();
	}

	@Override
	public void run()
	{
		timer++;

		borderManager.retract();
		
		if (borderManager.getSize() <= 75)
		{
			stop();
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
		timer = 0;
	}
	
	public int getTimer()
	{
		return timer;
	}
	
}
