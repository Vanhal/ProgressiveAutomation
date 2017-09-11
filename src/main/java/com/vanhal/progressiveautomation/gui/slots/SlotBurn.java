package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBurn extends Slot {

	public SlotBurn(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack) {
		return (BaseTileEntity.getItemBurnTime(itemStack)>0);
	}
}
