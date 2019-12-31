package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

import java.util.Random;

public class LeavesDecayListener implements Listener
{

    @EventHandler
    public void onLeaveDecay(final LeavesDecayEvent event)
    {
        final Block block = event.getBlock();
        final int i = new Random().nextInt(101);
        event.setCancelled(true);
        block.setType(Material.AIR);
        if (i <= 1)
        {
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemBuilder(Material.APPLE, 1).toItemStack());
        }
    }

}
