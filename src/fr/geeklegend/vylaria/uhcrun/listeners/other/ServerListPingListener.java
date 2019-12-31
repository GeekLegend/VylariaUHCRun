package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener
{

    @EventHandler
    public void onServerListPing(ServerListPingEvent event)
    {
        String status = null;

        switch (GameState.getState())
        {
            case WAITING:
                status = "En attente";
                break;
            case PREGAME:
            case GAME:
                status = "En jeu";
                break;
            case FINISH:
                status = "Fin de jeu";
                break;
            default:
                break;
        }
    }

}
