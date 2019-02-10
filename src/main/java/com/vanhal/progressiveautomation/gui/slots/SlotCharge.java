package com.vanhal.progressiveautomation.gui.slots;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCharge extends Slot {

    public SlotCharge(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyContainerItem) {
            return true;
        }
        return false;
    }
}