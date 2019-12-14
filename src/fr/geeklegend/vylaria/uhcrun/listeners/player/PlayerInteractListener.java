package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.api.VylariaAPI;
import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener
{

	private FileConfiguration config;

	private GameState gameState;

	public PlayerInteractListener()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.gameState = VylariaUHCRun.getInstance().getGameState();
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
				if (gameState.isState(GameState.WAITING))
				{
					if (item.getType() == Material.valueOf(config
							.getString("setups.join.items.leave.material").toUpperCase().replace(" ", "_")))
					{
						VylariaAPI.getInstance().getBungeeChannelApi().connect(player, "lobby");
					}
				}
			}
		}
	}

}
