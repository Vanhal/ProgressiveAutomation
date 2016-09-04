package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.gui.container.ContainerPlanter;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIPlanter extends BaseGUI {
	
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Planter.png");
	protected int infoScreenX = 38;
	protected int infoScreenW = 66;
	protected int infroScreenY1 = 17;
	protected int infroScreenY2 = 27;
	protected int infroScreenY3 = 37;
	
	TilePlanter planter;
	
	public GUIPlanter(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerPlanter(inv, entity), texture);
		planter = (TilePlanter) entity;
	}
	
	@Override
	protected void drawText() {
		drawString(StringHelper.localize("gui.planter"), 5, GRAY);
		drawString(StringHelper.localize("gui.range")+": "+StringHelper.getScaledNumber(planter.getRange()), infoScreenX, infoScreenW, infroScreenY3, (planter.hasUpgrade(UpgradeType.WITHER))?GREEN:WHITE);
		
		if (planter.isInvalidTool()) {
			drawString(getTextLine(1, "gui.invalidtool.1"), infoScreenX, infoScreenW, infroScreenY1, ORANGE);
			drawString(getTextLine(2, "gui.invalidtool.2"), infoScreenX, infoScreenW, infroScreenY2, ORANGE);
		} else if (planter.isLooked()) {
			boolean readyToPlant = false;
			if (planter.isFull()) {
				drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if ( (!planter.hasFuel()) && (!planter.isBurning()) ) {
				String fuelString = "gui.need.fuel";
				if (planter.hasEngine()) fuelString = "gui.need.energy";
				drawString(StringHelper.localize(fuelString), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if ( (planter.getStackInSlot(planter.SLOT_SEEDS) == null) && (!planter.isBurning()) ) {
				drawString(StringHelper.localize("gui.need.seeds"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else if (planter.getStackInSlot(planter.SLOT_HOE) == null) {
				drawString(StringHelper.localize("gui.need.hoe"), infoScreenX, infoScreenW, infroScreenY2, RED);
			} else {
				readyToPlant = true;
				String status = "gui.waiting";
				if (planter.getStatus()==1) status = "gui.harvesting";
				else if (planter.getStatus()==2) status = "gui.planting";
				drawString(StringHelper.localize(status), infoScreenX, infoScreenW, infroScreenY2, BLUE);
			}
			
			if (!readyToPlant) {
				drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
			} else if (planter.isBurning()) {
				drawString(StringHelper.localize("gui.running"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
			} else {
				drawString(StringHelper.localize("gui.waiting"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			}
		} else {
			drawString(getTextLine(1, "gui.hi.planter"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
			drawString(getTextLine(2, "gui.addtools"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
		}
	}
	
	@Override
	protected void drawElements() {
		drawFlame(planter.getPercentDone(), 10, 34);
	}
}
