package fr.geeklegend.vylaria.uhcrun;

import fr.geeklegend.vylaria.uhcrun.config.Config;
import fr.geeklegend.vylaria.uhcrun.game.BorderManager;
import fr.geeklegend.vylaria.uhcrun.game.CageManager;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import fr.geeklegend.vylaria.uhcrun.listeners.manager.ListenersManager;
import fr.geeklegend.vylaria.uhcrun.schedulers.*;
import fr.geeklegend.vylaria.uhcrun.scoreboard.ScoreboardManager;
import fr.geeklegend.vylaria.uhcrun.tablist.Tablist;
import fr.geeklegend.vylaria.uhcrun.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class VylariaUHCRun extends JavaPlugin
{

    public static VylariaUHCRun instance;

    private Config config;

    private ScheduledExecutorService executorMonoThread, scheduledExecutorService;

    private ScoreboardManager scoreboardManager;

    private GameState gameState;

    private GameManager gameManager;
    private BorderManager borderManager;
    private CageManager cageManager;

    private BorderScheduler borderScheduler;
    private BorderTimeScheduler borderTimeScheduler;
    private CageScheduler cageScheduler;
    private InvincibilityScheduler invincibilityScheduler;
    private PvPScheduler pvpScheduler;
    private StartScheduler startScheduler;
    private TimeScheduler timeScheduler;
    private WinScheduler winScheduler;

    private Tablist tablist;

    private WorldUtils worldUtils;

    @Override
    public void onEnable()
    {
        instance = this;

        executorMonoThread = Executors.newScheduledThreadPool(16);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager();

        config = new Config();
        config.create("config");
        config.load("config");
        config.getDefaultConfig().options().copyDefaults(true);
        config.getDefaultConfig().addDefault("schedulers.start.minplayers", 1);
        config.getDefaultConfig().addDefault("schedulers.start.timer", 30);
        config.getDefaultConfig().addDefault("schedulers.cages.timer", 5);
        config.getDefaultConfig().addDefault("schedulers.invincibility.timer", 30);
        config.getDefaultConfig().addDefault("schedulers.pvp.timer", 300);
        config.getDefaultConfig().addDefault("schedulers.border.timer", 900);
        config.getDefaultConfig().addDefault("messages.join",
                "&e%playername% &7a rejoint la partie. &e(%online%/%maxonline%)");
        config.getDefaultConfig().addDefault("messages.quit",
                "&e%playername% &7a quitté la partie. &e(%online%/%maxonline%)");
        config.getDefaultConfig().addDefault("messages.alreadystart",
                "&7La partie à déja commencer, vous avez rejoint en tant que spectateur.");
        config.getDefaultConfig().addDefault("messages.schedulers.start.while",
                "&7La partie commence dans &e%timer% seconde(s).");
        config.getDefaultConfig().addDefault("messages.schedulers.start.noenought",
                "&cIl n'y a pas assez de joueur pour commencer la partie.");
        config.getDefaultConfig().addDefault("messages.invincibility.start",
                "&7Vous êtes invincible pendant &e%timer% secondes.");
        config.getDefaultConfig().addDefault("messages.invincibility.while",
                "&7&7Vous êtes vulnérable dans &e%timer% seconde(s).");
        config.getDefaultConfig().addDefault("messages.invincibility.disabled",
                "&7Vous êtes désormais vulnérable.");
        config.getDefaultConfig().addDefault("messages.pvp",
                "&7Les dégâts entre joueurs sont désormais actifs.");
        config.getDefaultConfig().addDefault("messages.border",
                "&7La bordure commence à se rétracter.");
        config.getDefaultConfig().addDefault("messages.deathbykiller",
                "&e%victimname% §7a été tué par &c%killername%");
        config.getDefaultConfig().addDefault("messages.deathnokiller",
                "&e%victimname% §7est mort.");
        config.getDefaultConfig().addDefault("messages.win", "&7Le joueur &e%playername% &7a remporter la victoire !");
        config.getDefaultConfig().addDefault("game.world.name", "world");
        config.getDefaultConfig().addDefault("game.world.size", 800.0);
        config.getDefaultConfig().addDefault("setups.join.gamemode", "adventure");
        config.getDefaultConfig().addDefault("setups.join.spawn.y", 150.0);
        config.getDefaultConfig().addDefault("setups.join.spawn.z", 0.5);
        config.getDefaultConfig().addDefault("setups.join.spawn.yaw", 180);
        config.getDefaultConfig().addDefault("setups.join.spawn.pitch", 0);
        config.getDefaultConfig().addDefault("setups.join.items.leave.slot", 8);
        config.getDefaultConfig().addDefault("setups.join.items.leave.material", "bed");
        config.getDefaultConfig().addDefault("setups.join.items.leave.name", "&6Quitter");
        config.getDefaultConfig().addDefault("setups.game.gamemode", "survival");
        config.saveDefaultConfig();

        gameState = GameState.WAITING;

        worldUtils = new WorldUtils();

        tablist = new Tablist();

        gameManager = new GameManager();
        borderManager = new BorderManager();
        cageManager = new CageManager();

        borderScheduler = new BorderScheduler();
        borderTimeScheduler = new BorderTimeScheduler();
        cageScheduler = new CageScheduler();
        invincibilityScheduler = new InvincibilityScheduler();
        pvpScheduler = new PvPScheduler();
        startScheduler = new StartScheduler();
        timeScheduler = new TimeScheduler();
        winScheduler = new WinScheduler();

        gameManager.load();
        worldUtils.loadSchematic(new Location(Bukkit.getWorld(config.getDefaultConfig().getString("game.world.name")),
                config.getDefaultConfig().getDouble("setups.join.spawn.x"),
                config.getDefaultConfig().getDouble("setups.join.spawn.y") - 1,
                config.getDefaultConfig().getDouble("setups.join.spawn.z")), "uhcwtfwaitingcage", true);

        new ListenersManager(this).register();
    }

    @Override
    public void onDisable()
    {
        instance = null;

        scoreboardManager.onDisable();

        Bukkit.unloadWorld(config.getDefaultConfig().getString("game.world.name"), false);

        worldUtils.deleteWorld(new File(config.getDefaultConfig().getString("game.world.name")));
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

    public static VylariaUHCRun getInstance()
    {
        return instance;
    }

    public FileConfiguration getDefaultConfig()
    {
        return config.getDefaultConfig();
    }

    public GameState getGameState()
    {
        return gameState;
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

    public BorderScheduler getBorderScheduler()
    {
        return borderScheduler;
    }

    public BorderTimeScheduler getBorderTimeScheduler()
    {
        return borderTimeScheduler;
    }

    public CageScheduler getCageScheduler()
    {
        return cageScheduler;
    }

    public InvincibilityScheduler getInvincibilityScheduler()
    {
        return invincibilityScheduler;
    }

    public PvPScheduler getPvpScheduler()
    {
        return pvpScheduler;
    }

    public StartScheduler getStartScheduler()
    {
        return startScheduler;
    }

    public TimeScheduler getTimeScheduler()
    {
        return timeScheduler;
    }

    public WinScheduler getWinScheduler()
    {
        return winScheduler;
    }

    public Tablist getTablist()
    {
        return tablist;
    }

    public WorldUtils getWorldUtils()
    {
        return worldUtils;
    }
}
