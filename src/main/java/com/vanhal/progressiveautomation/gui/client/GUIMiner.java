package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
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
		drawString("Upgrades: "+miner.getUpgrades(), infoScreenX, infoScreenW, infroScreenY1, WHITE);
		boolean readyToMine = true;
		if ( miner.isInventoryFull() ) {
			drawString("Full", infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if ( (miner.getStackInSlot(0) == null) && (!miner.isBurning()) ) {
			drawString("Need Fuel", infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if (miner.getStackInSlot(1) == null) {
			drawString("Need Cobble", infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if (miner.getStackInSlot(2) == null) {
			drawString("Need Pickaxe", infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if (miner.getStackInSlot(3) == null) {
			drawString("Need Shovel", infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else {
			drawString(miner.getMinedBlocks()+" / "+miner.getMineBlocks(), infoScreenX, infoScreenW, infroScreenY2, BLUE);
		}
		
		if (!readyToMine) {
			drawString("Not Ready", infoScreenX, infoScreenW, infroScreenY1, RED);
		} else if (miner.getMinedBlocks()==miner.getMineBlocks()) {
			drawString("Finished", infoScreenX, infoScreenW, infroScreenY1, BLUE);
		} else if (miner.isSearching()) {
			drawString("Searching", infoScreenX, infoScreenW, infroScreenY1, GREEN);
		} else {
			drawString("Mining", infoScreenX, infoScreenW, infroScreenY1, GREEN);
		}
	}
	
	protected void drawElements() {
		drawFlame(miner.getPercentDone(), 10, 34);
	}
	
	
}
