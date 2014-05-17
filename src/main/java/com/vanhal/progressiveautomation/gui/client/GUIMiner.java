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
	protected int infoScreenX = 38;
	protected int infoScreenW = 66;
	protected int infroScreenY1 = 17;
	protected int infroScreenY2 = 27;
	protected int infroScreenY3 = 37;
	
	private TileMiner miner;

	public GUIMiner(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerMiner(inv, entity), texture);
		miner = (TileMiner) entity;
	}
	
	protected void drawText() {
		drawString("Miner", 5, GRAY);
		drawString("Status:", infoScreenX, infroScreenY1, WHITE);
		if (miner.getStackInSlot(0) == null) {
			drawString("Need Cobble", infoScreenX, infoScreenW, infroScreenY2, RED);
		} else if (miner.getStackInSlot(1) == null) {
			drawString("Need Fuel", infoScreenX, infoScreenW, infroScreenY2, RED);
		} /*else if (miner.getStackInSlot(2) == null) {
			drawString("Need Pickaxe", infoScreenX, infoScreenW, infroScreenY2, RED);
		} else if (miner.getStackInSlot(3) == null) {
			drawString("Need Shovel", infoScreenX, infoScreenW, infroScreenY2, RED);
		}*/ else {
			drawString(miner.getMinedBlocks()+" / "+miner.getMineBlocks(), infoScreenX, infoScreenW, infroScreenY2, BLUE);
		}
		
		drawString("Not Ready", infoScreenX, infoScreenW, infroScreenY3, RED);
	}
	
}
