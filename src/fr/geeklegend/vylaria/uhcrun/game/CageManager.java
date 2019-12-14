package fr.geeklegend.vylaria.uhcrun.game;

import fr.geeklegend.vylaria.api.cuboid.Cuboid;
import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CageManager
{

    private FileConfiguration config;

    public Cuboid WAITING_CAGE_CUBOID, PREGAME_CAGE_CUBOID;

    public CageManager()
    {
        this.config = VylariaUHCRun.getInstance().getDefaultConfig();
        this.WAITING_CAGE_CUBOID = new Cuboid(
                new Location(Bukkit.getWorld(config.getString("game.world.name")),
                        config.getDouble("setups.join.spawn.x") + 30,
                        config.getDouble("setups.join.spawn.y") + 30,
                        config.getDouble("setups.join.spawn.z") + 30),
                new Location(Bukkit.getWorld(config.getString("game.world.name")),
                        config.getDouble("setups.join.spawn.x") - 30,
                        config.getDouble("setups.join.spawn.y") - 30,
                        config.getDouble("setups.join.spawn.z") - 30));
        this.PREGAME_CAGE_CUBOID = null;
    }

    public void remove(boolean waiting)
    {
        if (waiting)
        {
            for (Block blocks : WAITING_CAGE_CUBOID.getBlocks())
            {
                if (blocks.getType() == Material.STAINED_GLASS)
                {
                    blocks.setType(Material.AIR);
                }
            }
        } else
        {
            for (Player players : Bukkit.getOnlinePlayers())
            {
                PREGAME_CAGE_CUBOID = new Cuboid(
                        new Location(players.getWorld(), players.getLocation().getBlockX() + 10,
                                players.getLocation().getBlockY() + 10, players.getLocation().getBlockZ() + 10),
                        new Location(players.getWorld(), players.getLocation().getBlockX() - 10,
                                players.getLocation().getBlockY() - 10, players.getLocation().getBlockZ() - 10));

                for (Block blocks : PREGAME_CAGE_CUBOID.getBlocks())
                {
                    if (blocks.getType() == Material.STAINED_GLASS)
                    {
                        blocks.setType(Material.AIR);
                    }
                }
            }
        }
    }

}
