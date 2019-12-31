package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener
{

	private GameManager gameManager;

	public AsyncPlayerChatListener()
	{
		this.gameManager = UHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		if (!GameState.isState(GameState.WAITING))
		{
			if (gameManager.spectatorsContains(player))
			{
				for (Player spectators : gameManager.getSpectators())
				{
					spectators.sendMessage("§7§o[Spectateur] " + player.getName() + ": " + message);
				}
				
				event.setCancelled(true);
			}
		} 
	}

}
