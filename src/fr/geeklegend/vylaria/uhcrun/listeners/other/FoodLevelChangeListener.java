package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener
{

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if (!GameState.isState(GameState.GAME))
		{
			event.setCancelled(true);
		}
	}
	
}
