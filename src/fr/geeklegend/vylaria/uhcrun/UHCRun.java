package fr.geeklegend.vylaria.uhcrun;

import fr.geeklegend.vylaria.uhcrun.game.BorderManager;
import fr.geeklegend.vylaria.uhcrun.game.CageManager;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import fr.geeklegend.vylaria.uhcrun.listeners.manager.ListenersManager;
import fr.geeklegend.vylaria.uhcrun.scoreboard.ScoreboardManager;
import fr.geeklegend.vylaria.uhcrun.tablist.Tablist;
import fr.geeklegend.vylaria.uhcrun.updater.Updater;
import fr.geeklegend.vylaria.uhcrun.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class UHCRun extends JavaPlugin
{

    public static UHCRun instance;

    private Updater updater;

    private WorldUtils worldUtils;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;
    private ScoreboardManager scoreboardManager;

    private GameManager gameManager;
    private BorderManager borderManager;
    private CageManager cageManager;

    private Tablist tablist;

    @Override
    public void onEnable()
    {
        instance = this;

        try
        {
            updater = new Updater(this, 356651, getFile(), true, false);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        GameState.setState(GameState.WAITING);

        worldUtils = new WorldUtils();
        tablist = new Tablist();
        tablist.setHealth();

        executorMonoThread = Executors.newScheduledThreadPool(16);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager();

        gameManager = new GameManager();
        gameManager.load();

        borderManager = new BorderManager();
        cageManager = new CageManager();

        new ListenersManager(this).register();
    }

    @Override
    public void onDisable()
    {
        instance = null;

        scoreboardManager.onDisable();

        if (GameState.isState(GameState.FINISH))
        {
            Bukkit.unloadWorld(getConfig().getString("game.world.name"), false);

            worldUtils.deleteWorld(new File(getConfig().getString("game.world.name")));
        }
    }

    public static UHCRun getInstance()
    {
        return instance;
    }

    public ScheduledExecutorService getExecutorMonoThread()
    {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService()
    {
        return scheduledExecutorService;
    }

    public ScoreboardManager getScoreboardManager()
    {
        return scoreboardManager;
    }

    public GameManager getGameManager()
    {
        return gameManager;
    }

    public BorderManager getBorderManager()
    {
        return borderManager;
    }

    public CageManager getCageManager()
    {
        return cageManager;
    }

    public WorldUtils getWorldUtils()
    {
        return worldUtils;
    }

    public Tablist getTablist()
    {
        return tablist;
    }
}
