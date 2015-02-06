package com.vanhal.progressiveautomation.blocks.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
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
        if (tag == null)
        {
        	buf.writeShort(-1);
        }
        else
        {
        	//compress doesn't seem to exist any more....
            //byte[] abyte = CompressedStreamTools.compress(tag);
            //buf.writeShort((short)abyte.length);
            //buf.writeBytes(abyte);
        }
    }

    /**
     * Reads a compressed NBTTagCompound from this buffer
     * @see PacketBuffer.readNBTTagCompoundFromBuffer()
     */
    private static NBTTagCompound readNBTTagCompoundFromBuffer(ByteBuf buf) throws IOException
    {
        short short1 = buf.readShort();

        if (short1 < 0)
        {
            return null;
        }
        else
        {
            byte[] abyte = new byte[short1];
            buf.readBytes(abyte);
            //I'm not really sure what this function does/did?
            //return CompressedStreamTools.func_152457_a(abyte, new NBTSizeTracker(2097152L));
            return null;
        }
    }

}
