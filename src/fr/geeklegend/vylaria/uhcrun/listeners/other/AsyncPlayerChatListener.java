package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener
{

	private GameState gameState;

	private GameManager gameManager;

	public AsyncPlayerChatListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		if (!gameState.isState(GameState.WAITING))
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
