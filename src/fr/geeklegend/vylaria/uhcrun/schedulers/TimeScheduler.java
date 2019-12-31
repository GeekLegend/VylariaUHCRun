package fr.geeklegend.vylaria.uhcrun.schedulers;

import org.bukkit.scheduler.BukkitRunnable;

public class TimeScheduler extends BukkitRunnable implements IScheduler
{
	
	private static int timer;

	public TimeScheduler()
	{
		this.timer = 0;
	}

	@Override
	public void run()
	{
		timer++;
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
	
	public static int getTimer()
	{
		return timer;
	}

}
