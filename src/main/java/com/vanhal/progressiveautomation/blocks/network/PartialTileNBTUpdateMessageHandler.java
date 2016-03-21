package com.vanhal.progressiveautomation.blocks.network;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PartialTileNBTUpdateMessageHandler implements IMessageHandler<PartialTileNBTUpdateMessage, IMessage> {

	@Override
	public IMessage onMessage(PartialTileNBTUpdateMessage message, MessageContext ctx) {
		TileEntity entity =  Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		
		if (entity != null && entity instanceof BaseTileEntity) {
			BaseTileEntity paEntity = (BaseTileEntity) entity;
			paEntity.readCommonNBT(message.nbtTag);
			paEntity.readSyncOnlyNBT(message.nbtTag);
		}
		return null;
	}

}
