package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.api.mysql.data.manager.PlayerDataManager;
import fr.geeklegend.vylaria.api.utils.ItemBuilder;
import fr.geeklegend.vylaria.uhcrun.VylariaUHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import fr.geeklegend.vylaria.uhcrun.schedulers.StartScheduler;
import fr.geeklegend.vylaria.uhcrun.tablist.Tablist;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener
{

	private FileConfiguration config;

	private GameState gameState;

	private GameManager gameManager;

	private Tablist tablist;

	private StartScheduler startScheduler;

	public PlayerJoinListener()
	{
		this.config = VylariaUHCRun.getInstance().getDefaultConfig();
		this.gameState = VylariaUHCRun.getInstance().getGameState();
		this.gameManager = VylariaUHCRun.getInstance().getGameManager();
		this.tablist = VylariaUHCRun.getInstance().getTablist();
		this.startScheduler = VylariaUHCRun.getInstance().getStartScheduler();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		PlayerDataManager.loadPlayerData(player);
		
		VylariaUHCRun.getInstance().getScoreboardManager().onLogin(player);

		if (gameState.isState(GameState.WAITING))
		{
			setup(player);
			
			tablist.setHealthScore(player);

			if (Bukkit.getOnlinePlayers().size() >= config.getInt("schedulers.start.minplayers"))
			{
				if (!startScheduler.isRunning())
				{
					startScheduler.setRunning(true);
					startScheduler.runTaskTimer(VylariaUHCRun.getInstance(), 20L, 20L);
				}
			}

			event.setJoinMessage(config.getString("messages.join").replace("&", "§")
					.replace("%playername%", player.getName())
					.replace("%online%", "" + Bukkit.getOnlinePlayers().size())
					.replace("%maxonline%", "" + Bukkit.getMaxPlayers()));
		} else
		{
			if (!gameManager.spectatorsContains(player))
			{
				gameManager.addSpectator(player);

				player.setGameMode(GameMode.SPECTATOR);
				player.setPlayerListName("§7§o[Spectateur] " + player.getName());
				player.teleport(new Location(Bukkit.getWorld(config.getString("game.world.name")), 0,
						100, 0));
				player.sendMessage(config.getString("messages.alreadystart").replace("&", "§"));
			}

			event.setJoinMessage(null);
		}
	}

	private void setup(Player player)
	{
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setStatistic(Statistic.PLAYER_KILLS, 0);
		player.setGameMode(GameMode.valueOf(config.getString("setups.join.gamemode").toUpperCase()));
		
		Location location = new Location(Bukkit.getWorld(config.getString("game.world.name")),
				config.getDouble("setups.join.spawn.x") + 13,
				config.getDouble("setups.join.spawn.y"),
				config.getDouble("setups.join.spawn.z") - 13,
				config.getInt("setups.join.spawn.yaw"),
				config.getInt("setups.join.spawn.pitch"));
		
		player.teleport(location);

		gameManager.addPreviousLocation(player, location);
		
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));

		giveItems(player);
	}

	private void giveItems(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setItem(config.getInt("setups.join.items.leave.slot"),
				new ItemBuilder(Material.valueOf(config.getString("setups.join.items.leave.material")
						.toUpperCase().replace(" ", "_"))).setName(
								config.getString("setups.join.items.leave.name").replace("&", "§"))
								.toItemStack());
	}

}
