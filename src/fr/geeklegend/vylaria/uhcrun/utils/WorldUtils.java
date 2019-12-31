package fr.geeklegend.vylaria.uhcrun.utils;

import java.io.*;

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

}
