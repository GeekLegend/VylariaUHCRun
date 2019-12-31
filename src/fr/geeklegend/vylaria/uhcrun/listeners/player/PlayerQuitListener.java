package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
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

	private GameManager gameManager;

	public PlayerQuitListener()
	{
		this.config = UHCRun.getInstance().getConfig();
		this.gameManager = UHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
	
		UHCRun.getInstance().getScoreboardManager().onLogout(player);
		
		if (!GameState.isState(GameState.GAME))
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
