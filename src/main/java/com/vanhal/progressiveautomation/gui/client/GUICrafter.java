package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.crafter.TileCrafter;
import com.vanhal.progressiveautomation.gui.container.ContainerCrafter;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUICrafter extends BaseGUI {
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Crafter.png");
	
	TileCrafter crafter;
	
	public GUICrafter(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerCrafter(inv, entity), texture);
		crafter = (TileCrafter) entity;
	}
	
	protected void drawText() {
		drawString(StringHelper.localize("gui.crafter"), 5, GRAY);
		
	}
	
	protected void drawElements() {
		drawFlame(crafter.getPercentDone(), 149, 34);
		drawProgress(crafter.getPercentCrafted(), 69, 16);
	}
	
	public void drawProgress(float progress, int x, int y) {
		int level = (int)Math.ceil(16*progress);
		if (level>16) level = 16;
		if (level<0) level = 0;
		drawTexturedModalRect(guiLeft + x, guiTop + y + 16 - level, 240, 0 + (16-level), 16, level);
	}
	
}
