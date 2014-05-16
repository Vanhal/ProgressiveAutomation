package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.gui.container.ContainerMiner;
import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIMiner extends BaseGUI {
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Miner.png");

	public GUIMiner(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerMiner(inv, entity), texture);
	}
	
}
