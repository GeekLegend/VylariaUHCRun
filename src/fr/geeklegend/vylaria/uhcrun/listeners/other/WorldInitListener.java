package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.game.populators.LobbyPopulator;
import fr.geeklegend.vylaria.uhcrun.game.populators.NetherPopulator;
import fr.geeklegend.vylaria.uhcrun.game.populators.OrePopulator;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;

public class WorldInitListener implements Listener
{

	@EventHandler
	public void onWorldInit(WorldInitEvent event)
	{
		World world = event.getWorld();

		new LobbyPopulator().createLobby(world, world.getChunkAt(0, 0));

		for (BlockPopulator blockPopulator : world.getPopulators())
		{
			if (blockPopulator instanceof OrePopulator)
			{
				return;
			}
			if (blockPopulator instanceof NetherPopulator)
			{
				return;
			}

			if (world.getEnvironment() == World.Environment.NORMAL)
			{
				world.getPopulators().add(new OrePopulator());
				world.getPopulators().add(new NetherPopulator());
			}
		}
	}

}
