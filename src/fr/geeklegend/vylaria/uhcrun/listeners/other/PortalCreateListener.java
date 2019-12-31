package fr.geeklegend.vylaria.uhcrun.listeners.other;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalCreateListener implements Listener
{

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event)
    {
        event.setCancelled(true);
    }

}
