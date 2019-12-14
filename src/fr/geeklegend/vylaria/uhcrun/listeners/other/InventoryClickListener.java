package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener
{

	private GameState gameState;

	public InventoryClickListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (!gameState.isState(GameState.GAME))
		{
			event.setCancelled(true);
		}
	}

}
