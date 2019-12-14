package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.api.mysql.data.manager.PlayerDataManager;
import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{

	private FileConfiguration config;

	private GameState gameState;

	private GameManager gameManager;

	public PlayerQuitListener()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
	
		PlayerDataManager.savePlayerData(player);
		
		VylariaUHCRun.getInstance().getScoreboardManager().onLogout(player);
		
		if (!gameState.isState(GameState.GAME))
		{
			event.setQuitMessage(config.getString("messages.quit").replace("&", "§")
					.replace("%playername%", player.getName())
					.replace("%online%", "" + (Bukkit.getOnlinePlayers().size() - 1))
					.replace("%maxonline%", "" + Bukkit.getMaxPlayers()));
		} else
		{
			if (gameManager.playersContains(player))
			{
				gameManager.removePlayer(player);
			}
			
			event.setQuitMessage(null);
		}
	}
	
}
