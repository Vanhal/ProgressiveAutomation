package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.farmer.TileFarmer;
import com.vanhal.progressiveautomation.gui.container.ContainerFarmer;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIFarmer extends BaseGUI {
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Farmer.png");
	protected int infoScreenX = 38;
	protected int infoScreenW = 66;
	protected int infroScreenY1 = 17;
	protected int infroScreenY2 = 27;
	protected int infroScreenY3 = 37;
	
	TileFarmer farmer;
	
	public GUIFarmer(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerFarmer(inv, entity), texture);
		farmer = (TileFarmer)entity;
	}
	
	@Override
	protected void drawText() {
		drawString(StringHelper.localize("gui.farmer"), 5, GRAY);
		drawString(StringHelper.localize("gui.range")+": "+StringHelper.getScaledNumber(farmer.getRange()), infoScreenX, infoScreenW, infroScreenY3, (farmer.hasUpgrade(UpgradeType.WITHER))?GREEN:WHITE);
	
		if (farmer.isInvalidTool()) {
			drawString(getTextLine(1, "gui.invalidtool.1"), infoScreenX, infoScreenW, infroScreenY1, ORANGE);
			drawString(getTextLine(2, "gui.invalidtool.2"), infoScreenX, infoScreenW, infroScreenY2, ORANGE);
		} else if (farmer.isLooked()) {
			boolean allGood = false;
			if (farmer.isFull()) {
				drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if ( (!farmer.hasFuel()) && (!farmer.isBurning()) ) {
				String fuelString = "gui.need.fuel";
				if (farmer.hasEngine()) fuelString = "gui.need.energy";
				drawString(StringHelper.localize(fuelString), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if ( (farmer.getStackInSlot(farmer.SLOT_FOOD) == null) && 
						(farmer.getStackInSlot(farmer.SLOT_BUCKETS) == null) && 
						(farmer.getStackInSlot(farmer.SLOT_SHEARS) == null) ) {
				drawString(StringHelper.localize("gui.need.items"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else {
				allGood = true;
				String status = "gui.waiting";
				if (farmer.getCurrentAction()==1) status = "gui.breeding";
				else if (farmer.getCurrentAction()==2) status = "gui.shearing";
				else if (farmer.getCurrentAction()==3) status = "gui.milking";
				drawString(StringHelper.localize(status), infoScreenX, infoScreenW, infroScreenY2, BLUE);
			}
			
			if (!allGood) {
				drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
			} else if (farmer.isBurning()) {
				drawString(StringHelper.localize("gui.running"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
			} else {
				drawString(StringHelper.localize("gui.waiting"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			}
	
		} else {
			drawString(getTextLine(1, "gui.hi.farmer"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			drawString(getTextLine(2, "gui.addtools"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
		}
	}
	
	@Override
	protected void drawElements() {
		drawFlame(farmer.getPercentDone(), 10, 34);
		//add the shearing input
		if (farmer.hasUpgrade(UpgradeType.SHEARING)) {
			drawTexturedModalRect(guiLeft + 36, guiTop + 51, 238, 18, 18, 18);
		}
		
		//add the buckets input
		if (farmer.hasUpgrade(UpgradeType.MILKER)) {
			drawTexturedModalRect(guiLeft + 62, guiTop + 51, 238, 0, 18, 18);
		}
	}
}
