package fr.geeklegend.vylaria.uhcrun.listeners.player;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.schedulers.WinScheduler;
import fr.geeklegend.vylaria.uhcrun.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener
{

	private FileConfiguration config;

	private GameManager gameManager;

	public PlayerDeathListener()
	{
		this.config = UHCRun.getInstance().getConfig();
		this.gameManager = UHCRun.getInstance().getGameManager();
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player victim = event.getEntity();
		Player killer = victim.getKiller();
		WinScheduler winScheduler = new WinScheduler();

		event.setDeathMessage(null);

		if (victim instanceof Player)
		{
			gameManager.removePlayer(victim);
			
			if (gameManager.getPlayers().size() == 1)
			{
				if (!winScheduler.isRunning())
				{
					winScheduler.setRunning(true);
					winScheduler.setPlayer(killer);
					winScheduler.runTaskTimer(UHCRun.getInstance(), 20L, 20L);
				}
			}

			if (killer != null)
			{
				victim.getWorld().dropItemNaturally(victim.getLocation(), new ItemBuilder(Material.SKULL_ITEM).setDurability((byte) 3).setSkullOwner(victim.getName()).toItemStack());

				if (killer == victim)
				{
						Bukkit.broadcastMessage(config.getString("messages.deathnokiller")
							.replace("&", "§").replace("%victimname%", victim.getName()));
				} else
				{
					Bukkit.broadcastMessage(config.getString("messages.deathbykiller")
							.replace("&", "§").replace("%victimname%", victim.getName()).replace("&", "§")
							.replace("%killername%", killer.getName()));
				}
			} else
			{
				Bukkit.broadcastMessage(config.getString("messages.deathnokiller").replace("&", "§")
						.replace("%victimname%", victim.getName()));
			}

			Bukkit.getScheduler().scheduleSyncDelayedTask(UHCRun.getInstance(), new Runnable()
			{
				public void run()
				{
					((CraftPlayer) victim).getHandle().playerConnection
							.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
				}
			}, 1L);

			killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 20.0f, 20.0f);
		}
	}

}
