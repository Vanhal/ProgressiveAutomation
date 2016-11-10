package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.gui.container.ContainerChopper;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIChopper extends BaseGUI {
	
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Chopper.png");
	protected int infoScreenX = 38;
	protected int infoScreenW = 66;
	protected int infroScreenY1 = 17;
	protected int infroScreenY2 = 27;
	protected int infroScreenY3 = 37;
	
	TileChopper chopper;
	
	public GUIChopper(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerChopper(inv, entity), texture);
		chopper = (TileChopper) entity;
	}
	
	protected void drawText() {
		drawString(StringHelper.localize("gui.chopper"), 5, GRAY);
		drawString(StringHelper.localize("gui.range")+": "+StringHelper.getScaledNumber(chopper.getRange()), infoScreenX, infoScreenW, infroScreenY3, (chopper.hasUpgrade(UpgradeType.WITHER))?GREEN:WHITE);
		
		if (chopper.isInvalidTool()) {
			drawString(getTextLine(1, "gui.invalidtool.1"), infoScreenX, infoScreenW, infroScreenY1, ORANGE);
			drawString(getTextLine(2, "gui.invalidtool.2"), infoScreenX, infoScreenW, infroScreenY2, ORANGE);
		} else if (chopper.isLooked()) {
			boolean readyToChop = false;
			if (chopper.isFull()) {
				drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if ( (!chopper.hasFuel()) && (!chopper.isBurning()) ) {
				String fuelString = "gui.need.fuel";
				if (chopper.hasEngine()) fuelString = "gui.need.energy";
				drawString(StringHelper.localize(fuelString), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if ( (chopper.getStackInSlot(chopper.SLOT_SAPLINGS) == null) && (!chopper.isBurning()) ) {
				drawString(StringHelper.localize("gui.need.sapling"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if (chopper.getStackInSlot(chopper.SLOT_AXE) == null) {
				drawString(StringHelper.localize("gui.need.axe"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else {
				readyToChop = true;
				String status = "gui.waiting";
				if (chopper.isChopping()) {
					status = "gui.chopping";
				} else if (chopper.isPlanting()) {
					status = "gui.planting";
				}
				drawString(StringHelper.localize(status), infoScreenX, infoScreenW, infroScreenY2, BLUE);
			}
			
			if (!readyToChop) {
				drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
			} else if (chopper.isBurning()) {
				drawString(StringHelper.localize("gui.running"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
			} else {
				drawString(StringHelper.localize("gui.waiting"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			}
		} else {
			drawString(getTextLine(1, "gui.hi.chopper"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			drawString(getTextLine(2, "gui.addtools"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
		}
	}
	
	
	
	protected void drawElements() {
		drawFlame(chopper.getPercentDone(), 10, 34);
		if (chopper.hasUpgrade(UpgradeType.SHEARING)) {
			drawTexturedModalRect(guiLeft + 62, guiTop + 51, 238, 0, 18, 18);
		}
	}
}
