package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.gui.container.ContainerMiner;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
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
		drawString(StringHelper.localize("gui.range")+": "+StringHelper.getScaledNumber(miner.getRange()), infoScreenX, infoScreenW, infroScreenY3, (miner.hasUpgrade(UpgradeType.WITHER))?GREEN:WHITE);
		
		if (miner.isInvalidTool()) {
			drawString(getTextLine(1, "gui.invalidtool.1"), infoScreenX, infoScreenW, infroScreenY1, ORANGE);
			drawString(getTextLine(2, "gui.invalidtool.2"), infoScreenX, infoScreenW, infroScreenY2, ORANGE);
		} else if (miner.isLooked()) {
			boolean readyToMine = true;
			if (miner.isFull()) {
				drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, RED);
				readyToMine = false;
			} else if ( (!miner.hasFuel()) && (!miner.isBurning()) ) {
				String fuelString = "gui.need.fuel";
				if (miner.hasEngine()) fuelString = "gui.need.energy";
				drawString(StringHelper.localize(fuelString), infoScreenX, infoScreenW, infroScreenY2, RED);
				readyToMine = false;
			} else if ( (miner.getStackInSlot(1) == null) && (!miner.hasUpgrade(UpgradeType.COBBLE_GEN)) ) {
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
			} else if (miner.getMinedBlocks() == miner.getMineBlocks()) {
				drawString(StringHelper.localize("gui.finished"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
			} else {
				drawString(StringHelper.localize("gui.mining"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			}
		} else {
			drawString(getTextLine(1, "gui.hi.miner"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			drawString(getTextLine(2, "gui.addtools"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
		}
		
	}
	
	protected void drawElements() {
		drawFlame(miner.getPercentDone(), 10, 34);
		if (miner.hasUpgrade(UpgradeType.COBBLE_GEN)) {
			drawTexturedModalRect(guiLeft - 25, guiTop + 10, 231, 0, 25, 64);
			
		}
	}
	
	
}
