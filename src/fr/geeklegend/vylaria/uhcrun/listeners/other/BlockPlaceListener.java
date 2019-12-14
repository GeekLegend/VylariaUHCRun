package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener
{

	private GameState gameState;

	public BlockPlaceListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (!gameState.isState(GameState.GAME))
		{
			event.setCancelled(true);
		}
	}
	
}
