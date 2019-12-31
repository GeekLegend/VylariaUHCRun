package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;

public class PrepareItemCraftListener implements Listener
{

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event)
    {
        Inventory inventory = event.getInventory();

        if (inventory instanceof CraftingInventory)
        {
            CraftingInventory craftingInventory = (CraftingInventory) inventory;

            switch (craftingInventory.getResult().getType())
            {
                case WOOD_SWORD:
                    craftingInventory.setResult(new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.DURABILITY, 1).toItemStack());
                    break;
                case WOOD_AXE:
                    craftingInventory.setResult(new ItemBuilder(Material.STONE_AXE).addEnchant(Enchantment.DURABILITY, 1).toItemStack());
                    break;
                case WOOD_PICKAXE:
                    craftingInventory.setResult(new ItemBuilder(Material.STONE_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).addEnchant(Enchantment.DURABILITY, 1).toItemStack());
                    break;
                case WOOD_SPADE:
                    craftingInventory.setResult(new ItemBuilder(Material.STONE_SPADE).addEnchant(Enchantment.DURABILITY, 1).toItemStack());
                    break;
                case WOOD_HOE:
                    craftingInventory.setResult(new ItemBuilder(Material.STONE_HOE).addEnchant(Enchantment.DURABILITY, 1).toItemStack());
                    break;
                default:
                    break;
            }
        }
    }

}
