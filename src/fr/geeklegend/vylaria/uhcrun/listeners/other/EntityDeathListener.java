package fr.geeklegend.vylaria.uhcrun.listeners.other;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Random;

public class EntityDeathListener implements Listener
{

    private Random random;

    public EntityDeathListener()
    {
        this.random = new Random();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        final EntityType mob = event.getEntityType();
        if (mob == EntityType.CHICKEN)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 3));
            event.getDrops().add(new ItemStack(Material.ARROW, 4));
        } else if (mob == EntityType.COW)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.COOKED_BEEF, 3));
            event.getDrops().add(new ItemStack(Material.LEATHER, 1));
        } else if (mob == EntityType.PIG)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
            event.getDrops().add(new ItemStack(Material.LEATHER, 1));
        } else if (mob == EntityType.SHEEP)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.COOKED_MUTTON, 3));
            event.getDrops().add(new ItemStack(Material.STRING, 1));
            if (random.nextInt(101) <= 1)
            {
                event.getDrops().add(new ItemStack(Material.LEATHER, 1));
            }
        } else if (mob == EntityType.RABBIT)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.COOKED_RABBIT, 3));
        } else if (mob == EntityType.SQUID)
        {
            event.getDrops().add(new ItemStack(Material.COOKED_FISH, 3));
        } else if (mob == EntityType.SPIDER || mob == EntityType.CAVE_SPIDER)
        {
            event.getDrops().add(new ItemStack(Material.STRING, random.nextInt(2) + 1));
        } else if (mob == EntityType.SKELETON)
        {
            event.getDrops().add(new ItemStack(Material.ARROW, random.nextInt(6) + 1));
        } else if (mob == EntityType.CREEPER)
        {
            event.getDrops().add(new ItemStack(Material.SULPHUR, 1));
        } else
        {
            if (mob != EntityType.WITCH)
            {
                return;
            }
            final Iterator<ItemStack> drops = event.getDrops().iterator();
            while (drops.hasNext())
            {
                final ItemStack current = drops.next();
                if (current.getType() == Material.GLOWSTONE_DUST)
                {
                    drops.remove();
                }
            }
        }
    }

}
