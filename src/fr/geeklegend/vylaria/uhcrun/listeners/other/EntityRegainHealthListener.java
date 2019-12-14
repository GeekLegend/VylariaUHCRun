package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class EntityRegainHealthListener implements Listener
{

	private GameState gameState;

	public EntityRegainHealthListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
	}
	
	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event)
	{
		RegainReason reason = event.getRegainReason();
		
		if (gameState.isState(GameState.GAME))
		{
			if (reason == RegainReason.SATIATED || reason == RegainReason.REGEN)
			{
				event.setCancelled(true);
			}
		}
	}

}
