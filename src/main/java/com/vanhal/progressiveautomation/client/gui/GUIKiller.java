package com.vanhal.progressiveautomation.client.gui;

import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.client.gui.container.ContainerKiller;
import com.vanhal.progressiveautomation.common.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.StringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIKiller extends BaseGUI {

    public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/gui/killer.png");
    protected int infoScreenX = 38;
    protected int infoScreenW = 66;
    protected int infroScreenY1 = 17;
    protected int infroScreenY2 = 27;
    protected int infroScreenY3 = 37;
    TileKiller killer;

    public GUIKiller(InventoryPlayer inv, TileEntity entity) {
        super(new ContainerKiller(inv, entity), texture);
        killer = (TileKiller) entity;
    }

    @Override
    protected void drawText() {
        drawString(StringHelper.localize("gui.killer"), 5, GRAY);
        drawString(StringHelper.localize("gui.range") + ": " + StringHelper.getScaledNumber(killer.getRange()), infoScreenX, infoScreenW, infroScreenY3, (killer.hasUpgrade(UpgradeType.WITHER)) ? GREEN : WHITE);
        if (killer.isInvalidTool()) {
            drawString(getTextLine(1, "gui.invalidtool.1"), infoScreenX, infoScreenW, infroScreenY1, ORANGE);
            drawString(getTextLine(2, "gui.invalidtool.2"), infoScreenX, infoScreenW, infroScreenY2, ORANGE);
        } else if (killer.isLooked()) {
            boolean readyToPlant = false;
            if (killer.isFull()) {
                drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, RED);
            } else if ((!killer.hasFuel()) && (!killer.isBurning())) {
                String fuelString = "gui.need.fuel";
                if (killer.hasEngine()) fuelString = "gui.need.energy";
                drawString(StringHelper.localize(fuelString), infoScreenX, infoScreenW, infroScreenY2, RED);
            } else if (killer.getStackInSlot(killer.SLOT_SWORD).isEmpty()) {
                drawString(StringHelper.localize("gui.need.sword"), infoScreenX, infoScreenW, infroScreenY2, RED);
            } else {
                readyToPlant = true;
                String status = "gui.waiting";
                if (killer.isKilling()) status = "gui.killing";
                drawString(StringHelper.localize(status), infoScreenX, infoScreenW, infroScreenY2, BLUE);
            }

            if (!readyToPlant) {
                drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
            } else if (killer.isBurning()) {
                drawString(StringHelper.localize("gui.running"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
            } else {
                drawString(StringHelper.localize("gui.waiting"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
            }
        } else {
            drawString(getTextLine(1, "gui.hi.killer"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
            drawString(getTextLine(2, "gui.addtools"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
        }
    }

    @Override
    protected void drawElements() {
        drawFlame(killer.getPercentDone(), 10, 22);
    }
}