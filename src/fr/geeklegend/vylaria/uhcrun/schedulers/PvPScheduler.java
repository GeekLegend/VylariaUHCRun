package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PvPScheduler extends BukkitRunnable implements IScheduler
{

    private FileConfiguration config;

    private static int timer;

    private static boolean running;

    private GameManager gameManager;

    public PvPScheduler()
    {
    	this.config = UHCRun.getInstance().getConfig();
        this.timer = config.getInt("schedulers.pvp.timer");
        this.running = false;
        this.gameManager = UHCRun.getInstance().getGameManager();
    }

    @Override
    public void run()
    {
        timer--;

        if (timer == 0)
        {
            stop();

            gameManager.setPvP(true);

            for (Player players : Bukkit.getOnlinePlayers())
            {
                players.playSound(players.getLocation(), Sound.ANVIL_USE, 20.0f, 20.0f);
                players.sendMessage(config.getString("messages.pvp").replace("&", "§"));
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

    public static int getTimer()
    {
        return timer;
    }

    public static boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

}
