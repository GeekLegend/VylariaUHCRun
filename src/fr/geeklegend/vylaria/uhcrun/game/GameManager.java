package fr.geeklegend.vylaria.uhcrun.game;

import com.google.common.collect.Lists;
import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.tablist.Tablist;
import fr.geeklegend.vylaria.uhcrun.utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager
{

    private FileConfiguration config;

    private List<String> biomes;

    private List<Player> spectators, players;

    private Map<Player, Location> previousLocation;

    private boolean isPvP, isBorder;

    private WorldUtils worldUtils;

    private Tablist tablist;

    public GameManager()
    {
        this.config = VylariaUHCRun.getInstance().getDefaultConfig();
        this.biomes = new ArrayList<String>();
        this.spectators = new ArrayList<Player>();
        this.players = new ArrayList<Player>();
        this.previousLocation = new HashMap<Player, Location>();
        this.isPvP = false;
        this.isBorder = false;
        this.worldUtils = VylariaUHCRun.getInstance().getWorldUtils();
        this.tablist = VylariaUHCRun.getInstance().getTablist();
    }

    public void load()
    {
       Bukkit.broadcastMessage("MDR");
        loadBiomes();

        worldUtils.changeBiome(getRandomBiome());
        WorldCreator worldCreator = new WorldCreator(config.getString("game.world.name"));
        worldCreator.environment(Environment.NORMAL);
        worldCreator.type(WorldType.LARGE_BIOMES);

        Bukkit.createWorld(worldCreator);

        World world = Bukkit.getWorld(config.getString("game.world.name"));
        world.setDifficulty(Difficulty.NORMAL);
        world.setTime(1000L);
        world.setStorm(false);
        world.setThundering(false);
        world.setGameRuleValue("doDaylightCycle", "false");

        clearEntities(world);

        tablist.setHealth();
    }

    public void clearEntities(World world)
    {
        for (Entity entity : world.getEntities())
        {
            entity.remove();
        }
    }

    public void loadBiomes()
    {
        biomes.add("jungle");
        biomes.add("mountains");
        biomes.add("desert");
        biomes.add("savanna");
        biomes.add("badlands");
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

            worldUtils.loadSchematic(
                    new Location(Bukkit.getWorld(config.getString("game.world.name")), x, y, z),
                    "uhcwtfsolocage", true);
        }
    }

    public String getRandomBiome()
    {
        List<String> givenList = Lists.newArrayList(biomes);
        int randomIndex = new Random().nextInt(givenList.size());
        String randomBiome = givenList.get(randomIndex);
        givenList.remove(randomIndex);
        return randomBiome;
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
