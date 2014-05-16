package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.gui.container.ContainerMiner;
import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIMiner extends BaseGUI {
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Miner.png");
	
	private TileMiner miner;

	public GUIMiner(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerMiner(inv, entity), texture);
		miner = (TileMiner) entity;
	}
	
	protected void drawText() {
		drawString("Miner", 6, GRAY);
		drawString("Status:", 38, 17, BLUE);
		drawString("<-Cobble", 38, 27, BLUE);
		drawString("Ready to Mine", 38, 37, BLUE);
	}
	
}
