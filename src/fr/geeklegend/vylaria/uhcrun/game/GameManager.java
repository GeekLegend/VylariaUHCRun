package fr.geeklegend.vylaria.uhcrun.game;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.tablist.Tablist;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager
{

    private FileConfiguration config;

    private List<Player> spectators;
    private List<Player> players;

    private Map<Player, Location> previousLocation;

    private boolean isPvP;
    private boolean isBorder;

    private Tablist tablist;

    public GameManager()
    {
        this.config = UHCRun.getInstance().getConfig();
        this.spectators = new ArrayList<Player>();
        this.players = new ArrayList<Player>();
        this.previousLocation = new HashMap<Player, Location>();
        this.isPvP = false;
        this.isBorder = false;
        this.tablist = UHCRun.getInstance().getTablist();
    }

    public void load()
    {
        WorldCreator worldCreator = new WorldCreator(config.getString("game.world.name"));
        worldCreator.environment(Environment.NORMAL);
        worldCreator.type(WorldType.LARGE_BIOMES);

        World w = Bukkit.createWorld(worldCreator);

        World world = Bukkit.getWorld(config.getString("game.world.name"));
        world.setDifficulty(Difficulty.NORMAL);
        world.setTime(1000L);
        world.setStorm(false);
        world.setThundering(false);
        world.setGameRuleValue("doDaylightCycle", "false");

        clearEntities(world);
    }

    public void clearEntities(World world)
    {
        for (Entity entity : world.getEntities())
        {
            entity.remove();
        }
    }

    public void createWaiting()
    {
        World world = Bukkit.getWorld(config.getString("game.world.name"));
        Location minLocation = new Location(world,
                config.getDouble("setups.join.spawn.x"),
                config.getDouble("setups.join.spawn.y"),
                config.getDouble("setups.join.spawn.z"));
    }

    public void deleteWaiting()
    {
        World world = Bukkit.getWorld(config.getString("game.world.name"));
        Location minLocation = new Location(world,
                config.getDouble("setups.join.spawn.x"),
                config.getDouble("setups.join.spawn.y"),
                config.getDouble("setups.join.spawn.z"));
        Location maxLocation = new Location(world,
                config.getDouble("setups.join.spawn.x") - 50,
                config.getDouble("setups.join.spawn.y") - 50,
                config.getDouble("setups.join.spawn.z") - 50);

        int minX = Math.min(minLocation.getBlockX(), maxLocation.getBlockX());
        int minY = Math.min(minLocation.getBlockY(), maxLocation.getBlockY());
        int minZ = Math.min(minLocation.getBlockZ(), maxLocation.getBlockZ());

        int maxX = Math.max(minLocation.getBlockX(), maxLocation.getBlockX());
        int maxY = Math.max(minLocation.getBlockY(), maxLocation.getBlockY());
        int maxZ = Math.max(minLocation.getBlockZ(), maxLocation.getBlockZ());

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    Block block = world.getBlockAt(x, y, z);

                    if (block.getType() == Material.STAINED_GLASS)
                    {
                        block.setType(Material.AIR);

                        for (Entity entity : world.getEntities())
                        {
                            entity.remove();
                        }
                    }
                }
            }
        }
    }

    public void preGameSetup()
    {
        for (Player players : Bukkit.getOnlinePlayers())
        {
            addPlayer(players);

            players.setLevel(0);
            players.getInventory().clear();
        }

        teleport();
    }

    public void gameSetup()
    {
        for (Player players : Bukkit.getOnlinePlayers())
        {
            players.setGameMode(
                    GameMode.valueOf(config.getString("setups.game.gamemode").toUpperCase()));
        }
    }

    public void teleport()
    {
        for (Player players : Bukkit.getOnlinePlayers())
        {
            Random random = new Random();

            int x = players.getLocation().getBlockX() + random.nextInt(200);
            int y = 150;
            int z = players.getLocation().getBlockZ() + random.nextInt(200);

            Location location = new Location(Bukkit.getWorld(config.getString("game.world.name")),
                    x + 1.5, y, z + 1);

            players.teleport(location);

            addPreviousLocation(players, location);
        }
    }

    public void addPlayer(Player player)
    {
        players.add(player);
    }

    public void removePlayer(Player player)
    {
        players.remove(player);
    }

    public boolean playersContains(Player player)
    {
        return players.contains(player);
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void addSpectator(Player player)
    {
        spectators.add(player);
    }

    public void removeSpectator(Player player)
    {
        spectators.remove(player);
    }

    public boolean spectatorsContains(Player player)
    {
        return spectators.contains(player);
    }

    public List<Player> getSpectators()
    {
        return spectators;
    }

    public Location getPreviousLocation(Player player)
    {
        return previousLocation.get(player);
    }

    public void addPreviousLocation(Player player, Location location)
    {
        previousLocation.put(player, location);
    }

    public void removePreviousLocation(Player player)
    {
        previousLocation.remove(player);
    }

    public boolean previousLocationContains(Player player)
    {
        return previousLocation.containsKey(player);
    }

    public boolean isPvP()
    {
        return isPvP;
    }

    public void setPvP(boolean isPvP)
    {
        this.isPvP = isPvP;
    }

    public boolean isBorder()
    {
        return isBorder;
    }

    public void setBorder(boolean isBorder)
    {
        this.isBorder = isBorder;
    }

}
