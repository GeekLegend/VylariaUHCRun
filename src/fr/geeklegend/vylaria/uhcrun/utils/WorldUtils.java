package fr.geeklegend.vylaria.uhcrun.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;

public class WorldUtils
{

    public void copyFolder(File src, File dest) throws IOException
    {

        if (src.isDirectory())
        {

            if (!dest.exists())
            {
                dest.mkdir();
            }

            String files[] = src.list();

            for (String file : files)
            {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }

        } else
        {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    public boolean deleteWorld(File path)
    {
        if (path.exists())
        {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isDirectory())
                {
                    deleteWorld(files[i]);
                } else
                {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public void changeBiome(String biome)
    {
        try
        {
            String mojangPath = "net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            Class<?> clazz = Class.forName(mojangPath + ".BiomeBase");

            Field plainsField = clazz.getDeclaredField(biome);
            plainsField.setAccessible(true);
            Object plainsBiome = plainsField.get(null);

            Field biomesField = clazz.getDeclaredField("biomes");
            biomesField.setAccessible(true);
            Object[] biomes = (Object[]) biomesField.get(null);

            for (int i = 0; i < biomes.length; i++)
            {
                biomes[i] = plainsBiome;
            }
            biomesField.set(null, biomes);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadSchematic(Location location, String name, boolean ignoreAirBlocks)
    {
        File file = new File("./plugins/WorldEdit/schematics/" + name + ".schematic");
        Vector to = new Vector(location.getX(), location.getY(), location.getZ());
        World weWorld = new BukkitWorld(Bukkit.getWorld("world"));
        WorldData worldData = weWorld.getWorldData();
        try
        {
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
            Region region = clipboard.getRegion();
            EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            AffineTransform transform = new AffineTransform();
            ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(),
                    extent, to);
            if (!transform.isIdentity())
            {
                copy.setTransform(transform);
            }
            if (ignoreAirBlocks)
            {
                copy.setSourceMask(new ExistingBlockMask(clipboard));
            }
            Operations.completeLegacy(copy);
            extent.flushQueue();
        } catch (IOException | MaxChangedBlocksException e)
        {
            e.printStackTrace();
        }
    }

}
