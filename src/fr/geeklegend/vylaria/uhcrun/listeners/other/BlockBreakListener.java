package fr.geeklegend.vylaria.uhcrun.listeners.other;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.GameManager;
import fr.geeklegend.vylaria.uhcrun.game.GameState;
import fr.geeklegend.vylaria.uhcrun.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class BlockBreakListener implements Listener
{

    private GameManager gameManager;
    private final Random random;
    public static final BlockFace[] BLOCK_FACES = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_WEST};
    ;

    public BlockBreakListener()
    {
        this.gameManager = UHCRun.getInstance().getGameManager();
        this.random = new Random();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        if (GameState.isState(GameState.WAITING))
        {
            event.setCancelled(true);
        } else if (GameState.isState(GameState.GAME))
        {
            if (block != null)
            {
                switch (block.getType())
                {
                    case SAND:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        blockLocation.getWorld().dropItemNaturally(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemBuilder(Material.GLASS_BOTTLE, 1).toItemStack());
                        break;
                    case IRON_ORE:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        blockLocation.getWorld().dropItemNaturally(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemBuilder(Material.IRON_INGOT, 2).toItemStack());
                        player.giveExp(3);
                        break;
                    case GOLD_ORE:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        blockLocation.getWorld().dropItemNaturally(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemBuilder(Material.GOLD_INGOT, 2).toItemStack());
                        player.giveExp(4);
                        break;
                    case DIAMOND_ORE:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        blockLocation.getWorld().dropItemNaturally(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemBuilder(Material.DIAMOND, 2).toItemStack());
                        player.giveExp(6);
                        break;
                    case COAL_ORE:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        blockLocation.getWorld().dropItemNaturally(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemBuilder(Material.TORCH, 4).toItemStack());
                        player.giveExp(2);
                        break;
                    case LAPIS_ORE:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        blockLocation.getWorld().dropItemNaturally(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemBuilder(Material.INK_SACK, 5).setDurability((byte) 4).toItemStack());
                        player.giveExp(6);
                        break;
                    case GRAVEL:
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        if (Math.random() < 0.4D)
                        {
                            blockLocation.getWorld().dropItem(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemStack(Material.ARROW, 4));
                        } else if (Math.random() < 0.7D)
                        {
                            blockLocation.getWorld().dropItem(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemStack(Material.FLINT, 1));
                        } else
                        {
                            blockLocation.getWorld().dropItem(blockLocation.add(0.5D, 0.5D, 0.5D), new ItemStack(Material.GRAVEL, 1));
                        }
                        break;
                    case OBSIDIAN:
                        if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                        {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 400, 2));
                        } else
                        {
                            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                        }
                        break;
                    default:
                        break;
                }

                if (block.getType() == Material.LOG || block.getType() == Material.LOG_2)
                {
                    final Location loc = event.getBlock().getLocation();
                    final World world = loc.getWorld();
                    final int x = loc.getBlockX();
                    final int y = loc.getBlockY();
                    final int z = loc.getBlockZ();
                    final int range = 4;
                    final int off = 5;
                    if (!this.validChunk(world, x - 5, y - 5, z - 5, x + 5, y + 5, z + 5))
                    {
                        return;
                    }
                    Bukkit.getServer().getScheduler().runTask(UHCRun.getInstance(), new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (int offX = -4; offX <= 4; ++offX)
                            {
                                for (int offY = -4; offY <= 4; ++offY)
                                {
                                    for (int offZ = -4; offZ <= 4; ++offZ)
                                    {
                                        if (world.getBlockTypeIdAt(x + offX, y + offY, z + offZ) == Material.LEAVES.getId() || world.getBlockTypeIdAt(x + offX, y + offY, z + offZ) == Material.LEAVES_2.getId())
                                        {
                                            breakLeaf(world, player, x + offX, y + offY, z + offZ);
                                        }
                                    }
                                }
                            }
                        }
                    });
                    breakTree(event.getBlock(), event.getPlayer());
                }
            }
        }
    }

    public void breakTree(final Block log, final Player player)
    {
        if (log.getType() != Material.LOG && log.getType() != Material.LOG_2)
        {
            return;
        }
        log.breakNaturally();
        BlockBreakEvent event = null;
        BlockFace[] values;
        for (int length = (values = BlockFace.values()).length, i = 0; i < length; ++i)
        {
            final BlockFace face = values[i];
            if (log.getRelative(face).getType() == Material.LOG || log.getRelative(face).getType() == Material.LOG_2)
            {
                event = new BlockBreakEvent(log.getRelative(face), player);
                Bukkit.getServer().getPluginManager().callEvent(event);
                event = new BlockBreakEvent(log.getRelative(face).getRelative(BlockFace.UP), player);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }
        }
    }

    private void breakLeaf(final World world, final Player player, final int x, final int y, final int z)
    {
        final Block block = world.getBlockAt(x, y, z);
        final byte range = 4;
        final byte max = 32;
        final int[] blocks = new int[max * max * max];
        final int off = range + 1;
        final int mul = max * max;
        final int div = max / 2;
        if (this.validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
        {
            for (int offX = -range; offX <= range; ++offX)
            {
                for (int offY = -range; offY <= range; ++offY)
                {
                    for (int offZ = -range; offZ <= range; ++offZ)
                    {
                        final int type = world.getBlockTypeIdAt(x + offX, y + offY, z + offZ);
                        blocks[(offX + div) * mul + (offY + div) * max + offZ + div] = ((type == Material.LOG.getId() || type == Material.LOG_2.getId()) ? 0 : ((type == Material.LEAVES.getId() || type == Material.LEAVES_2.getId()) ? -2 : -1));
                    }
                }
            }
            for (int offX = 1; offX <= 4; ++offX)
            {
                for (int offY = -range; offY <= range; ++offY)
                {
                    for (int offZ = -range; offZ <= range; ++offZ)
                    {
                        for (int type = -range; type <= range; ++type)
                        {
                            if (blocks[(offY + div) * mul + (offZ + div) * max + type + div] == offX - 1)
                            {
                                if (blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] == -2)
                                {
                                    blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] == -2)
                                {
                                    blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] == -2)
                                {
                                    blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] == -2)
                                {
                                    blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] == -2)
                                {
                                    blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] == -2)
                                {
                                    blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] = offX;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (blocks[div * mul + div * max + div] < 0)
        {
            final Location location = block.getLocation();
            final LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getServer().getPluginManager().callEvent(event);
            player.getWorld().playEffect(location, Effect.STEP_SOUND, Material.LEAVES);
        }
    }

    public boolean validChunk(final World world, int minX, final int minY, int minZ, int maxX, final int maxY, int maxZ)
    {
        if (maxY >= 0 && minY < world.getMaxHeight())
        {
            minX >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxZ >>= 4;
            for (int x = minX; x <= maxX; ++x)
            {
                for (int z = minZ; z <= maxZ; ++z)
                {
                    if (!world.isChunkLoaded(x, z))
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
