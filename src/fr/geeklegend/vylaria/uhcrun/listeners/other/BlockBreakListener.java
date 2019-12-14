package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener
{

	private GameState gameState;

	private GameManager gameManager;

	public BlockBreakListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		
		if (gameState.isState(GameState.WAITING))
		{
			event.setCancelled(true);
		} else if (gameState.isState(GameState.GAME))
		{
			if (block != null)
			{
			}
		}
	}

}
