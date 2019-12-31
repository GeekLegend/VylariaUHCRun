package fr.geeklegend.vylaria.uhcrun.config;

import fr.geeklegend.vylaria.uhcrun.UHCRun;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config
{

    private FileConfiguration defaultConfig;

    public Config()
    {
        this.defaultConfig = YamlConfiguration.loadConfiguration(getFile("config"));
    }

    public void create(String fileName)
    {
        if (!UHCRun.getInstance().getDataFolder().exists())
        {
            UHCRun.getInstance().getDataFolder().mkdir();
        }
        File file = new File(UHCRun.getInstance().getDataFolder(), fileName + ".yml");
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void load(String fileName)
    {
        YamlConfiguration.loadConfiguration(getFile(fileName));
    }

    public void saveDefaultConfig()
    {
        try
        {
            defaultConfig.save(getFile("config"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public File getFile(String fileName)
    {
        return new File(UHCRun.getInstance().getDataFolder(), fileName + ".yml");
    }

    public FileConfiguration getDefaultConfig()
    {
        return defaultConfig;
    }

}