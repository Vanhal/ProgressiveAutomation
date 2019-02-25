package com.vanhal.progressiveautomation.client.gui;

import com.vanhal.progressiveautomation.common.entities.capacitor.TileCapacitor;
import com.vanhal.progressiveautomation.client.gui.container.ContainerCapacitor;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.common.util.StringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GUICapacitor extends BaseGUI {

    public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/gui/capacitor.png");
    protected int infoScreenX = 68;
    protected int infoScreenW = 87;
    protected int infroScreenY1 = 21;
    protected int infroScreenY2 = 31;
    TileCapacitor capacitor;

    public GUICapacitor(InventoryPlayer inv, TileEntity entity) {
        super(new ContainerCapacitor(inv, entity), texture);
        capacitor = (TileCapacitor) entity;
        setHeight(135);
    }

    protected void drawText() {
    	IEnergyStorage cap = capacitor.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
        drawString(StringHelper.localize("gui.capacitor"), 5, GRAY);
        drawString(StringHelper.localize("gui.maximum") + ": " + capacitor.getTransferRate() + " FE/t", infoScreenX, infoScreenW, infroScreenY1, GREEN);
        if ((cap.getEnergyStored() >= cap.getMaxEnergyStored())) {
            drawString(StringHelper.getScaledNumber(cap.getEnergyStored(), 100) + " / " + StringHelper.getScaledNumber(cap.getMaxEnergyStored()) + " FE", infoScreenX, infoScreenW, infroScreenY2, RED);
        } else {
            drawString(StringHelper.getScaledNumber(cap.getEnergyStored(), 100) + " / " + StringHelper.getScaledNumber(cap.getMaxEnergyStored()) + " FE", infoScreenX, infoScreenW, infroScreenY2, BLUE);
        }
    }
}