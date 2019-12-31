package fr.geeklegend.vylaria.uhcrun.game.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class OrePopulator extends BlockPopulator
{

    private static final int[] ITERATIONS = new int[]{15, 10, 8, 4, 4, 2};
    private static final Material[] TYPES = new Material[]
            {
                    Material.REDSTONE_ORE,
                    Material.DIAMOND_ORE,
                    Material.GOLD_ORE,
                    Material.IRON_ORE,
                    Material.COAL_ORE,
                    Material.OBSIDIAN
            };

    private static final int[] MAX_HEIGHT = new int[]{
            64, 64, 64, 64, 64, 64
    };

    private static int[] AMOUNTS = new int[]
            {
                    10, 10, 30, 5, 10, 4
            };

    @Override

    public void populate(World world, Random random, Chunk chunk)
    {
        for (int i = 0; i < TYPES.length; i++)
        {
            for (int j = 0; i < ITERATIONS[i]; j++)
            {
                makeOres(chunk, random, random.nextInt(16), random.nextInt(MAX_HEIGHT[i]), random.nextInt(16), AMOUNTS[i], TYPES[i]);
            }
        }
    }

    private void makeOres(Chunk chunk, Random random, int originX, int originY, int originZ, int amount, Material type)
    {
        for (int i = 0; i < amount; i++)
        {
            int x = originX + random.nextInt(amount / 2) - amount / 4;
            int y = originY + random.nextInt(amount / 4) - amount / 8;
            int z = originZ + random.nextInt(amount / 2) - amount / 4;
            x &= 0xf;
            z &= 0xf;

            if (y > 127 || y < 0)
            {
                continue;
            }

            Block block = chunk.getBlock(x, y, z);

            if (block.getType() == Material.SAND)
            {
                block.setType(type, false);
            }
        }
    }

}
