package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import fr.geeklegend.vylaria.uhcrun.schedulers.InvincibilityScheduler;
import fr.geeklegend.vylaria.uhcrun.schedulers.PvPScheduler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageListener implements Listener
{

	private GameManager gameManager;

	public EntityDamageListener()
	{
		this.gameManager = UHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		DamageCause damageCause = event.getCause();

		if (GameState.isState(GameState.WAITING) || GameState.isState(GameState.PREGAME))
		{
			event.setCancelled(true);
		} else if (GameState.isState(GameState.GAME))
		{
			if (InvincibilityScheduler.isRunning())
			{
				event.setCancelled(true);
			} else
			{
				if (!gameManager.isPvP())
				{
					if (damageCause == DamageCause.LAVA || damageCause == DamageCause.FIRE
							|| damageCause == DamageCause.FIRE_TICK)
					{
						event.setCancelled(true);
					}
				}
				if (damageCause != DamageCause.ENTITY_ATTACK || !(entity instanceof Player))
				{
					return;
				} else
				{
					if (PvPScheduler.isRunning())
					{
						event.setCancelled(true);
					} 
				}
			}
		}
	}

}
