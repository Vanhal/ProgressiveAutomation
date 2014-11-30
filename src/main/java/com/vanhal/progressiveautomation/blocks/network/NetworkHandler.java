package com.vanhal.progressiveautomation.blocks.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import com.vanhal.progressiveautomation.ref.Ref;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {
	
	public static final int NEARBY = 8;
	public static final int MEDIUM = 16;
	public static final int FAR = 64;
	
	private NetworkHandler() {
	}
	
	private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("pAutomation");
	private static int discriminationByte = 1;
	
	public static <REQ extends IMessage> void registerMessageHandler(IMessageHandler<? super REQ, ? extends IMessage> messageHandler, Class<REQ> requestMessageType, Side side) {
		CHANNEL.registerMessage(messageHandler, requestMessageType, discriminationByte++, side);
	}
	
	public static <REQ extends IMessage, REPLY extends IMessage> void registerMessageHandler(
			Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		CHANNEL.registerMessage(messageHandler, requestMessageType, discriminationByte++, side);
	}
	
	public static void sendToAllAroundNearby(IMessage message, TileEntity entity) {
		CHANNEL.sendToAllAround(message, new TargetPoint(entity.getWorldObj().provider.dimensionId, entity.xCoord, entity.yCoord, entity.zCoord, NEARBY));
	}
	
	public static void sendToPlayer(IMessage message, EntityPlayerMP player) {
		CHANNEL.sendTo(message, player);
	}

}
