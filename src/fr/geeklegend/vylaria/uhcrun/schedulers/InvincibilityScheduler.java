package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InvincibilityScheduler extends BukkitRunnable implements IScheduler
{

	private FileConfiguration config;

	private int timer;

	private boolean running;

	public InvincibilityScheduler()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.timer = config.getInt("schedulers.invincibility.timer");
		this.running = false;
	}

	@Override
	public void run()
	{
		timer--;

		if (timer == 5 || timer == 4 || timer == 2 || timer == 1)
		{
			Bukkit.broadcastMessage(config.getString("messages.invincibility.while").replace("&", "§")
					.replace("%timer%", "" + timer));
		} else if (timer == 0)
		{
			stop();

			for (Player players : Bukkit.getOnlinePlayers())
			{
				players.playSound(players.getLocation(), Sound.IRONGOLEM_DEATH, 20.0f, 20.0f);
				players.sendMessage(
						config.getString("messages.invincibility.disabled").replace("&", "§"));
			}
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
		timer = config.getInt("schedulers.invincibility.timer");
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
