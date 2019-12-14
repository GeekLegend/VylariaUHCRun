package fr.geeklegend.vylaria.uhcrun.game;

import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;

public class BorderManager
{

    private FileConfiguration config;

    private WorldBorder worldBorder;

    private int size ;

    private boolean isMoved;

    public BorderManager()
    {
        this.config = VylariaUHCRun.getInstance().getDefaultConfig();
        this.worldBorder = null;
        this.size = config.getInt("game.world.size");
        this.isMoved = false;
    }

    public void load()
    {
        World world = Bukkit.getWorld(config.getString("game.world.name"));
        worldBorder = world.getWorldBorder();
        worldBorder.setCenter(0.0, 0.0);
        worldBorder.setSize(1600);
    }

    public void retract()
    {
        worldBorder.setSize(worldBorder.getSize() - 1);
        size--;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public boolean isMoved()
    {
        return isMoved;
    }

    public void setMoved(boolean isMoved)
    {
        this.isMoved = isMoved;
    }

}
