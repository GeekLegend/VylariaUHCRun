package fr.geeklegend.vylaria.uhcrun.game;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SchematicManager
{

    private byte[] blocks, metadata;
    private short width, height, length;

    public void loadSchematic(DataInput in) throws Exception
    {
        NBT_Tag compound = NBT_Tag.readTag(in);
        NBT_Tag_Compound tag = (NBT_Tag_Compound) compound;

        this.width = ((NBT_Tag_Short) tag.payload.get("Width")).payload;
        this.height = ((NBT_Tag_Short) tag.payload.get("Height")).payload;
        this.length = ((NBT_Tag_Short) tag.payload.get("Length")).payload;

        this.blocks = ((NBT_Tag_Byte_Array) tag.payload.get("Blocks")).payload;
        this.metadata = ((NBT_Tag_Byte_Array) tag.payload.get("Data")).payload;
    }

    public void writeUncompressedSchematic(DataOutput out) throws IOException
    {
        NBT_Tag_Compound root = new NBT_Tag_Compound("Schematic");
        root.payload.put("Width", new NBT_Tag_Short("Width", this.width));
        root.payload.put("Height", new NBT_Tag_Short("Height", this.height));
        root.payload.put("Length", new NBT_Tag_Short("Length", this.length));

        root.payload.put("Blocks", new NBT_Tag_Byte_Array("Blocks", this.blocks));
        root.payload.put("Data", new NBT_Tag_Byte_Array("Data", this.metadata));
        root.writeTag(out);
    }

    public void writeUncompressedSchematic(File f) throws IOException
    {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
        writeUncompressedSchematic(out);
        out.close();
    }

    public void writeGzipedSchematic(File f) throws IOException
    {
        DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
        writeUncompressedSchematic(out);
        out.close();
    }

    public void loadGzipedSchematic(InputStream is) throws Exception
    {
        DataInputStream in = new DataInputStream(new GZIPInputStream(is));
        loadSchematic(in);
        in.close();
    }

    public void loadUncompressedSchematic(InputStream is) throws Exception
    {
        DataInputStream in = new DataInputStream(is);
        loadSchematic(in);
        in.close();
    }

    private int getBlockOffset(int x, int y, int z)
    {
        return y * width * length + z * width + x;
    }

    public byte getBlockIdAt(int x, int y, int z)
    {
        int offset = getBlockOffset(x, y, z);
        if (offset < this.blocks.length && offset >= 0)
            return this.blocks[offset];
        else
            return -1;
    }

    public void setBlockIdAt(int x, int y, int z, byte id)
    {
        int offset = getBlockOffset(x, y, z);
        if (offset < this.blocks.length && offset >= 0)
            this.blocks[offset] = id;
    }

    public byte getMetadataAt(int x, int y, int z)
    {
        int offset = getBlockOffset(x, y, z);
        if (offset < this.metadata.length && offset >= 0)
            return this.metadata[offset];
        else
            return 0;
    }

    public void setMetadataIdAt(int x, int y, int z, byte data)
    {
        int offset = getBlockOffset(x, y, z);
        if (offset < this.metadata.length && offset >= 0)
            this.metadata[offset] = data;
    }

    public short getWidth()
    {
        return width;
    }

    public void setWidth(short width)
    {
        this.width = width;
    }

    public short getHeight()
    {
        return height;
    }

    public void setHeight(short height)
    {
        this.height = height;
    }

    public short getLength()
    {
        return length;
    }

    public void setLength(short length)
    {
        this.length = length;
    }

    public byte[] getBlocks()
    {
        return blocks;
    }

    public void setBlocks(byte[] blocks)
    {
        this.blocks = blocks;
    }

    public byte[] getMetadata()
    {
        return metadata;
    }

    public void setMetadata(byte[] metadata)
    {
        this.metadata = metadata;
    }

}


class NBT_Tag_End extends NBT_Tag
{

    public NBT_Tag_End(String name)
    {
        super(0, "");
    }

    public NBT_Tag_End(String name, int payload)
    {
        super(8, name);
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        System.out.println("An error has occoured. An named binary tree tag 'end' has had it's payload read. It doesn't have a payload. Fix your code :D");
    }

    @Override
    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
    }

    public void writePayload(DataOutput out) throws IOException
    {
    }

}

class NBT_Tag_Double extends NBT_Tag
{

    public double payload;

    public NBT_Tag_Double(String name)
    {
        super(6, name);
    }

    public NBT_Tag_Double(String name, double payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readDouble();
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.writeDouble(this.payload);
    }
}

class NBT_Tag_Compound extends NBT_Tag
{

    public Map<String, NBT_Tag> payload;

    public NBT_Tag_Compound(String name)
    {
        super(10, name);
    }

    public Map<String, NBT_Tag> getPayload()
    {
        return payload;
    }

    public NBT_Tag_Compound(String name, Map<String, NBT_Tag> payload)
    {
        super(8, name);
        this.payload = payload;
    }

    public NBT_Tag_Compound(Map<String, NBT_Tag> payload)
    {
        super(8, "");
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        payload = new HashMap<String, NBT_Tag>();
        NBT_Tag tag;

        while ((tag = NBT_Tag.readTag(in)).id != 0)
        {
            this.payload.put(tag.name, tag);
        }
        this.payload.put("__end", new NBT_Tag_End("__end"));
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        for (String key : payload.keySet())
        {
            NBT_Tag tag = payload.get(key);
            tag.writeTag(out);
        }
    }
}

class NBT_Tag_Byte extends NBT_Tag
{

    public byte payload;

    public NBT_Tag_Byte(String name)
    {
        super(1, name);
    }

    public NBT_Tag_Byte(String name, byte payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readByte();
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.write(this.payload);
    }
}

class NBT_Tag_Byte_Array extends NBT_Tag
{

    public int size;
    public byte[] payload;

    public NBT_Tag_Byte_Array(String name)
    {
        super(7, name);
    }

    public NBT_Tag_Byte_Array(String name, byte[] payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        int size = in.readInt();
        this.size = size;
        this.payload = new byte[size];

        in.readFully(this.payload);
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        out.writeInt(this.size);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        for (byte i : this.payload)
        {
            out.writeByte(i);
        }
    }
}

class NBT_Tag_String extends NBT_Tag
{

    public String payload;

    public NBT_Tag_String(String name)
    {
        super(8, name);
    }

    public NBT_Tag_String(String name, String payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readUTF();
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.writeUTF(this.payload);
    }

    public String getPayload()
    {
        return payload;
    }
}

class NBT_Tag_Short extends NBT_Tag
{

    public short payload;

    public NBT_Tag_Short(String name)
    {
        super(2, name);
    }

    public NBT_Tag_Short(String name, short payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readShort();
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.writeShort(this.payload);
    }

}

class NBT_Tag_Long extends NBT_Tag
{

    public long payload;

    public NBT_Tag_Long(String name)
    {
        super(4, name);
    }

    public NBT_Tag_Long(String name, Long payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readLong();
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.writeLong(this.payload);
    }

}

class NBT_Tag_List extends NBT_Tag
{

    public byte tag_type;
    public int size;
    public List<NBT_Tag> payload;

    public NBT_Tag_List(String name)
    {
        super(9, name);
    }

    public NBT_Tag_List(String name, List<NBT_Tag> payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.tag_type = in.readByte();
        int size = in.readInt();
        this.size = size;
        this.payload = new ArrayList<NBT_Tag>();

        for (int i = 0; i < size; i++)
        {
            NBT_Tag tag = NBT_Tag.getNewTag(this.tag_type, "");
            tag.readTagPayload(in);
            this.payload.add(tag);
        }
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        out.writeInt(this.size);

        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        for (NBT_Tag tag : this.payload)
        {
            tag.writePayload(out);
        }
    }
}

class NBT_Tag_Int extends NBT_Tag
{

    public int payload;

    public NBT_Tag_Int(String name)
    {
        super(3, name);
    }

    public int getPayload()
    {
        return payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readInt();
    }

    public NBT_Tag_Int(String name, int payload)
    {
        super(8, name);
        this.payload = payload;
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.writeInt(this.payload);
    }

}

class NBT_Tag_Int_Array extends NBT_Tag
{

    public int size;
    public int[] payload;

    public NBT_Tag_Int_Array(String name)
    {
        super(11, name);
    }

    public NBT_Tag_Int_Array(String name, int[] payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        int size = in.readInt();
        this.size = size;
        this.payload = new int[size];

        for (int i = 0; i < size; i++)
        {
            this.payload[i] = in.readInt();
        }
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        out.writeInt(this.size);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        for (int i : this.payload)
        {
            out.writeInt(i);
        }
    }
}

class NBT_Tag_Float extends NBT_Tag
{

    public float payload;

    public NBT_Tag_Float(String name)
    {
        super(5, name);
    }

    public NBT_Tag_Float(String name, float payload)
    {
        super(8, name);
        this.payload = payload;
    }

    @Override
    public void readTagPayload(DataInput in) throws IOException
    {
        this.payload = in.readFloat();
    }

    public void writeTag(DataOutput out) throws IOException
    {
        out.write(this.id);
        out.writeUTF(this.name);
        this.writePayload(out);
    }

    public void writePayload(DataOutput out) throws IOException
    {
        out.writeFloat(this.payload);
    }

}

abstract class NBT_Tag
{

    public static NBT_Tag getNewTag(int id, String name)
    {
        switch (id)
        {
            case 0:
                return new NBT_Tag_End("");
            case 1:
                return new NBT_Tag_Byte(name);
            case 2:
                return new NBT_Tag_Short(name);
            case 3:
                return new NBT_Tag_Int(name);
            case 4:
                return new NBT_Tag_Long(name);
            case 5:
                return new NBT_Tag_Float(name);
            case 6:
                return new NBT_Tag_Double(name);
            case 7:
                return new NBT_Tag_Byte_Array(name);
            case 8:
                return new NBT_Tag_String(name);
            case 9:
                return new NBT_Tag_List(name);
            case 10:
                return new NBT_Tag_Compound(name);
            case 11:
                return new NBT_Tag_Int_Array(name);
            default:
                return null;
        }
    }

    public static NBT_Tag readTag(DataInput in) throws IOException
    {
        NBT_Tag tag;
        byte tag_id = in.readByte();
        if (tag_id == 0) return new NBT_Tag_End("");
        String tag_name = in.readUTF();
        tag = getNewTag(tag_id, tag_name);
        tag.readTagPayload(in);
        return tag;
    }

    byte id;

    String name;

    public NBT_Tag(String name)
    {
        this.id = 0;
        this.name = name;
    }

    protected NBT_Tag(int id, String name)
    {
        this.id = (byte) id;
        this.name = name;
    }

    public abstract void readTagPayload(DataInput in) throws IOException;

    public abstract void writeTag(DataOutput out) throws IOException;

    public abstract void writePayload(DataOutput out) throws IOException;
}