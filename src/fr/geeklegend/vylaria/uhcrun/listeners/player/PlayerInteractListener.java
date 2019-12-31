package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractListener implements Listener
{

	private FileConfiguration config;

	public PlayerInteractListener()
	{
		this.config = UHCRun.getInstance().getConfig();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack item = event.getItem();

		if (item != null)
		{
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
			{
				if (GameState.isState(GameState.WAITING))
				{
					if (item.getType() == Material.valueOf(config
							.getString("setups.join.items.leave.material").toUpperCase().replace(" ", "_")))
					{
						//VylariaUHCRun.getInstance().getBungeeChannelApi().connect(player, "lobby");
					}
				} else if (GameState.isState(GameState.GAME))
				{
					if (item.getType() == Material.SKULL_ITEM)
					{
						player.getInventory().remove(item);
						player.updateInventory();
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
					}
				}
			}
		}
	}

}
