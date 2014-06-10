package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.gui.container.ContainerMiner;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.util.StringHelper;

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
		drawString(StringHelper.localize("gui.miner"), 5, GRAY);
		drawString(StringHelper.localize("gui.range")+": "+StringHelper.getScaledNumber(miner.getRange()), infoScreenX, infoScreenW, infroScreenY3, WHITE);
		boolean readyToMine = true;
		if ( (miner.getStackInSlot(0) == null) && (!miner.isBurning()) ) {
			drawString(StringHelper.localize("gui.need.fuel"), infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if (miner.getStackInSlot(1) == null) {
			drawString(StringHelper.localize("gui.need.cobble"), infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if (miner.getStackInSlot(miner.SLOT_PICKAXE) == null) {
			drawString(StringHelper.localize("gui.need.pick"), infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else if (miner.getStackInSlot(miner.SLOT_SHOVEL) == null) {
			drawString(StringHelper.localize("gui.need.shovel"), infoScreenX, infoScreenW, infroScreenY2, RED);
			readyToMine = false;
		} else {
			drawString(StringHelper.getScaledNumber(miner.getMinedBlocks())+"/"+StringHelper.getScaledNumber(miner.getMineBlocks()), infoScreenX, infoScreenW, infroScreenY2, BLUE);
		}
		
		if (!readyToMine) {
			drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
		} else if (miner.getMinedBlocks()==miner.getMineBlocks()) {
			drawString(StringHelper.localize("gui.finished"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
		} else {
			drawString(StringHelper.localize("gui.mining"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
		}
	}
	
	protected void drawElements() {
		drawFlame(miner.getPercentDone(), 10, 34);
	}
	
	
}
