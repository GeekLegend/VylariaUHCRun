package fr.geeklegend.vylaria.uhcrun.listeners.manager;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.listeners.other.*;
import fr.geeklegend.vylaria.uhcrun.listeners.player.*;
import org.bukkit.plugin.PluginManager;

public class ListenersManager
{

	private UHCRun instance;
	
	public ListenersManager(UHCRun instance)
	{
		this.instance = instance;
	}
	
	public void register()
	{
		PluginManager pluginManager = instance.getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(), instance);
		pluginManager.registerEvents(new PlayerQuitListener(), instance);
		pluginManager.registerEvents(new PlayerDropItemListener(), instance);
		pluginManager.registerEvents(new PlayerMoveListener(), instance);
		pluginManager.registerEvents(new PlayerDeathListener(), instance);
		pluginManager.registerEvents(new PlayerRespawnListener(), instance);
		pluginManager.registerEvents(new PlayerInteractListener(), instance);
		pluginManager.registerEvents(new PlayerItemConsumeListener(), instance);

		pluginManager.registerEvents(new ServerListPingListener(), instance);
        pluginManager.registerEvents(new LeavesDecayListener(), instance);
		pluginManager.registerEvents(new PortalCreateListener(), instance);
		pluginManager.registerEvents(new PrepareItemCraftListener(), instance);
		pluginManager.registerEvents(new WorldInitListener(), instance);
		pluginManager.registerEvents(new AsyncPlayerChatListener(), instance);
		pluginManager.registerEvents(new BlockBreakListener(), instance);
		pluginManager.registerEvents(new BlockPlaceListener(), instance);
		pluginManager.registerEvents(new FoodLevelChangeListener(), instance);
		pluginManager.registerEvents(new EntityDamageByEntityListener(), instance);
		pluginManager.registerEvents(new EntityDamageListener(), instance);
		pluginManager.registerEvents(new EntityDeathListener(), instance);
		pluginManager.registerEvents(new EntityRegainHealthListener(), instance);
		pluginManager.registerEvents(new InventoryClickListener(), instance);
		pluginManager.registerEvents(new WeatherChangeListener(), instance);
	}
	
}
