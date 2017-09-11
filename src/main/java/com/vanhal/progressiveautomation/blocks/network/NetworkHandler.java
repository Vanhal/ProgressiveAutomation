package com.vanhal.progressiveautomation.blocks.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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
		CHANNEL.sendToAllAround(message, new TargetPoint(entity.getWorld().provider.getDimension(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), NEARBY));
	}
	
	public static void sendToPlayer(IMessage message, EntityPlayerMP player) {
		CHANNEL.sendTo(message, player);
	}

}
