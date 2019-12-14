package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerRespawnListener implements Listener
{

	private GameState gameState;

	private GameManager gameManager;

	public PlayerRespawnListener()
	{
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		Player killer = player.getKiller();

		if (!gameState.isState(GameState.WAITING))
		{
			if (!gameManager.spectatorsContains(player))
			{
				gameManager.addSpectator(player);

				player.setGameMode(GameMode.SPECTATOR);
				player.setPlayerListName("§7§o[Spectateur] " + player.getName());
				player.getInventory().clear();

				if (killer != null)
				{
					event.setRespawnLocation(killer.getLocation());
				} else
				{
					List<Player> players = new ArrayList<Player>();

					for (Player pls : Bukkit.getOnlinePlayers())
					{
						players.add(pls);

						Player randomPlayer = players.get(new Random().nextInt(players.size()));

						event.setRespawnLocation(randomPlayer.getLocation());
					}
				}
			}
		}
	}

}
