package com.vanhal.progressiveautomation.entities;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.WorldServer;

public class FakePlayer extends EntityPlayerMP {

	public FakePlayer(WorldServer world) {
		super(
			FMLCommonHandler.instance().getMinecraftServerInstance(),
			world,
			new GameProfile("023154A4-C831-98BC-E227-920192839203", "-PAMiner-"), 
			new ItemInWorldManager(world)
		);
		this.addedToChunk = false;
	}
}
