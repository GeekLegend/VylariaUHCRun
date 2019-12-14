package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener
{

	private GameState gameState;

	private GameManager gameManager;

	public PlayerMoveListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		if (gameState.isState(GameState.WAITING) || gameState.isState(GameState.PREGAME))
		{
			if (player.getLocation().getY() < 140)
			{
				player.teleport(gameManager.getPreviousLocation(player));
			}
		}
	}

}
