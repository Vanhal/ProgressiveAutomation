package com.vanhal.progressiveautomation.blocks.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PartialTileNBTUpdateMessage implements IMessage {
	
	int x;
	int y;
	int z;
	NBTTagCompound nbtTag;
	
	public PartialTileNBTUpdateMessage() {
	}
	
	public PartialTileNBTUpdateMessage(int x, int y, int z, NBTTagCompound nbtTag) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.nbtTag = nbtTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();

			nbtTag = readNBTTagCompoundFromBuffer(buf);
		} catch (IOException exception) {
			throw new IllegalStateException("The impossible has happened!", exception);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
			
			writeNBTTagCompoundToBuffer(buf, nbtTag);
		} catch (IOException exception) {
			throw new IllegalStateException("The impossible has happened!", exception);
		}
	}
	
	/**
     * Writes a compressed NBTTagCompound to this buffer. 
     * @see PacketBuffer.writeNBTTagCompoundToBuffer()
     */
    private static void writeNBTTagCompoundToBuffer(ByteBuf buf, NBTTagCompound tag) throws IOException
    {
        if (tag == null) {
        	buf.writeShort(-1);
        } else {
        	//compress doesn't seem to exist any more....
            byte[] abyte = compress(tag);
            buf.writeShort((short)abyte.length);
            buf.writeBytes(abyte);
        }
    }

    /**
     * Reads a compressed NBTTagCompound from this buffer
     * @see PacketBuffer.readNBTTagCompoundFromBuffer()
     */
    private static NBTTagCompound readNBTTagCompoundFromBuffer(ByteBuf buf) throws IOException
    {
        short short1 = buf.readShort();

        if (short1 < 0)  {
            return null;
        } else  {
            byte[] abyte = new byte[short1];
            buf.readBytes(abyte);
            
            return decompress(abyte, new NBTSizeTracker(2097152L));
        }
    }
    
    private static byte[] compress(NBTTagCompound p_74798_0_) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));

        try {
        	CompressedStreamTools.write(p_74798_0_, dataoutputstream);
        } finally  {
            dataoutputstream.close();
        }

        return bytearrayoutputstream.toByteArray();
    }
    
    public static NBTTagCompound decompress(byte[] p_152457_0_, NBTSizeTracker p_152457_1_) throws IOException {
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(p_152457_0_))));
        NBTTagCompound nbttagcompound;

        try {
            nbttagcompound = CompressedStreamTools.read(datainputstream, p_152457_1_);
        } finally {
            datainputstream.close();
        }

        return nbttagcompound;
    }

}
