package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener
{

	private GameState gameState;

	public PlayerDropItemListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if (!gameState.isState(GameState.GAME))
		{
			event.setCancelled(true);
		}
	}

}
