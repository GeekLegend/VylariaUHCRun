package fr.geeklegend.vylaria.uhcrun.game.populators;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import fr.geeklegend.vylaria.uhcrun.game.SchematicManager;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.InputStream;

public class LobbyPopulator
{

    private String fileName = "spawn.schematic";

    public void createLobby(World world, Chunk chunk)
    {
        if (chunk.getX() % 20 == 0 && chunk.getZ() % 20 == 0)
        {
            try
            {
                InputStream inputStream = UHCRun.getInstance().getClass().getClassLoader().getResourceAsStream(fileName);
                SchematicManager schematicManager = new SchematicManager();
                schematicManager.loadGzipedSchematic(inputStream);

                int width = schematicManager.getWidth();
                int height = schematicManager.getHeight();
                int length = schematicManager.getLength();
                int startY = 10;
                int endY = startY + height;

                for (int x = 0; x < width; x++)
                {
                    for (int z = 0; z < length; z++)
                    {
                        int realX = x + chunk.getX() * 16;
                        int realZ = z + chunk.getZ() * 16;

                        for (int y = startY; y <= endY && y < 255; y++)
                        {
                            int realY = y - startY;
                            int id = schematicManager.getBlockIdAt(x, realY, z);
                            byte data = schematicManager.getMetadataAt(x, realY, z);

                            if (id == -103) world.getBlockAt(realX, y, realZ).setTypeIdAndData(153, data, true);
                            if (id > -1 && world.getBlockAt(realX, y, realZ) != null) world.getBlockAt(realX, y, realZ).setTypeIdAndData(id, data, true);
                        }
                    }
                }
                inputStream.close();
            } catch (Exception e)
            {
                System.out.println("Could not read the schematic file");
                e.printStackTrace();
            }
        }
    }
}
