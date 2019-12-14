package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
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

	private GameState gameState;

	private GameManager gameManager;

	private InvincibilityScheduler invincibilityScheduler;
	private PvPScheduler pvpScheduler;

	public EntityDamageListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
		this.invincibilityScheduler = VylariaUHCRun.getInstance().getInvincibilityScheduler();
		this.pvpScheduler = VylariaUHCRun.getInstance().getPvpScheduler();
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		DamageCause damageCause = event.getCause();

		if (gameState.isState(GameState.WAITING) || gameState.isState(GameState.PREGAME))
		{
			event.setCancelled(true);
		} else if (gameState.isState(GameState.GAME))
		{
			if (invincibilityScheduler.isRunning())
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
					if (pvpScheduler.isRunning())
					{
						event.setCancelled(true);
					} 
				}
			}
		}
	}

}
