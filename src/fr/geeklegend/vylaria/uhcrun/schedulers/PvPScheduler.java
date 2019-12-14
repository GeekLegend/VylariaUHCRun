package fr.geeklegend.vylaria.uhcrun.schedulers;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PvPScheduler extends BukkitRunnable implements IScheduler
{

    private FileConfiguration config;

    private int timer;

    private boolean running;

    private GameManager gameManager;

    public PvPScheduler()
    {
    	this.config = VylariaUHCRun.getInstance().getDefaultConfig();
        this.timer = config.getInt("schedulers.pvp.timer");
        this.running = false;
        this.gameManager = VylariaUHCRun.getInstance().getGameManager();
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
