package com.vanhal.progressiveautomation.client.gui.container;

import com.vanhal.progressiveautomation.client.gui.slots.SlotCharge;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.entities.capacitor.TileCapacitor;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerCapacitor extends BaseContainer {

    TileCapacitor capacitor;

    public ContainerCapacitor(InventoryPlayer inv, TileEntity entity) {
        super((BaseTileEntity) entity);
        capacitor = (TileCapacitor) entity;
        addSlotToContainer(new SlotCharge(capacitor, capacitor.SLOT_CHARGER, 29, 23)); //charger
        addPlayerInventory(inv, 53);
    }
}