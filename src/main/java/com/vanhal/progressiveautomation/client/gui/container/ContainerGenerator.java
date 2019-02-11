package com.vanhal.progressiveautomation.client.gui.container;

import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.client.gui.slots.SlotCharge;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerGenerator extends BaseContainer {

    TileGenerator generator;

    public ContainerGenerator(InventoryPlayer inv, TileEntity entity) {
        super((BaseTileEntity) entity, 29, 23, false);
        generator = (TileGenerator) entity;
        addSlotToContainer(new SlotCharge(generator, generator.SLOT_CHARGER, 8, 23)); //charger
        addPlayerInventory(inv, 53);
    }
}