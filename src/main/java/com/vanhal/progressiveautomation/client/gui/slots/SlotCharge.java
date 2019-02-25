package com.vanhal.progressiveautomation.client.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;

public class SlotCharge extends Slot {

    public SlotCharge(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
            return true;
        }
        return false;
    }
}