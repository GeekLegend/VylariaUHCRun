package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener
{

    private GameManager gameManager;

    public PlayerMoveListener()
    {
        this.gameManager = UHCRun.getInstance().getGameManager();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if (GameState.isState(GameState.FINISH))
        {
            return;
        } else
        {
			if (player.getLocation().getY() < 140)
			{
				player.teleport(gameManager.getPreviousLocation(player));
			}
        }
    }

}
