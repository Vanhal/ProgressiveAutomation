package com.vanhal.progressiveautomation.client.gui;

import com.vanhal.progressiveautomation.common.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.client.gui.container.ContainerGenerator;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.common.util.StringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GUIGenerator extends BaseGUI {

    public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/gui/generator.png");
    protected int infoScreenX = 68;
    protected int infoScreenW = 87;
    protected int infroScreenY1 = 16;
    protected int infroScreenY2 = 26;
    protected int infroScreenY3 = 36;
    TileGenerator generator;

    public GUIGenerator(InventoryPlayer inv, TileEntity entity) {
        super(new ContainerGenerator(inv, entity), texture);
        generator = (TileGenerator) entity;
        setHeight(135);
    }

    protected void drawText() {
    	IEnergyStorage cap = generator.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
        drawString(StringHelper.localize("gui.generator"), 5, GRAY);
        drawString(StringHelper.getScaledNumber(cap.getEnergyStored(), 100) + " / " + StringHelper.getScaledNumber(cap.getMaxEnergyStored()) + " FE", infoScreenX, infoScreenW, infroScreenY3, RED);
        boolean ready = false;
        if ((!generator.hasFuel()) && (!generator.isBurning())) {
            drawString(StringHelper.localize("gui.need.fuel"), infoScreenX, infoScreenW, infroScreenY2, RED);
        } else if ((cap.getEnergyStored() >= cap.getMaxEnergyStored()) && (!generator.isBurning())) {
            ready = true;
            drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
        } else {
            ready = true;
            drawString("+" + generator.getProduceRate() + " FE/t", infoScreenX, infoScreenW, infroScreenY2, BLUE);
        }

        if (!ready) {
            drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
        } else if (generator.isBurning()) {
            drawString(StringHelper.localize("gui.running"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
        } else {
            drawString(StringHelper.localize("gui.waiting"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
        }
    }

    protected void drawElements() {
        drawFlame(generator.getPercentDone(), 47, 22);
    }
}