package com.vanhal.progressiveautomation.gui.client;

import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitor;
import com.vanhal.progressiveautomation.gui.container.ContainerCapacitor;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUICapacitor extends BaseGUI {
	
public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Capacitor.png");
	
	protected int infoScreenX = 68;
	protected int infoScreenW = 87;
	protected int infroScreenY1 = 21;
	protected int infroScreenY2 = 31;
	
	TileCapacitor capacitor;

	public GUICapacitor(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerCapacitor(inv, entity), texture);
		capacitor = (TileCapacitor)entity;
		this.setHeight(135);
	}

	protected void drawText() {
		drawString(StringHelper.localize("gui.capacitor"), 5, GRAY);
		drawString(StringHelper.localize("gui.maximum")+": "+capacitor.getTransferRate()+" RF/t", infoScreenX, infoScreenW, infroScreenY1, GREEN);
		if((capacitor.getEnergyStored()>=capacitor.getMaxEnergyStored())){
			drawString(StringHelper.getScaledNumber(capacitor.getEnergyStored(), 100) + " / " + StringHelper.getScaledNumber(capacitor.getMaxEnergyStored()) + " RF", infoScreenX, infoScreenW, infroScreenY2, RED);
		}else{
			drawString(StringHelper.getScaledNumber(capacitor.getEnergyStored(), 100) + " / " + StringHelper.getScaledNumber(capacitor.getMaxEnergyStored()) + " RF", infoScreenX, infoScreenW, infroScreenY2, BLUE);
		}
	}

}
