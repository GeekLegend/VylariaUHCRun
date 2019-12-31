package fr.geeklegend.vylaria.uhcrun.game;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WinManager
{

	private FileConfiguration config;

	private Player player;

	private GameState gameState;

	private GameManager gameManager;

	public WinManager(Player player)
	{
		this.config = UHCRun.getInstance().getConfig();
		this.player = player;
		this.gameState = gameState.getState();
		this.gameManager = UHCRun.getInstance().getGameManager();
	}

	public void check()
	{
		gameState.setState(GameState.FINISH);
		
		gameManager.clearEntities(Bukkit.getWorld(config.getString("game.world.name")));

		Bukkit.getScheduler().cancelAllTasks();
		Bukkit.broadcastMessage(config.getString("messages.win").replace("&", "§")
				.replace("%playername%", player.getName()));

		Bukkit.getScheduler().runTaskLater(UHCRun.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				for (Player players : Bukkit.getOnlinePlayers())
				{
					//VylariaUHCRun.getInstance().getBungeeChannelApi().connect(players, "lobby");
				}
			}
		}, 100L);

		Bukkit.getScheduler().runTaskLater(UHCRun.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.getServer().shutdown();
			}
		}, 120L);
	}

}
