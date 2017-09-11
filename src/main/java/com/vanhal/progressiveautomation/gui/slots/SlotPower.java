package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.items.ItemRFEngine;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPower extends Slot {

	public SlotPower(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack) {
		//check if it burnable
		boolean isCorrect = (BaseTileEntity.getItemBurnTime(itemStack)>0);
		//check if it is a RFEngine
		if (!isCorrect) {
			if (itemStack.getItem() instanceof ItemRFEngine) {
				isCorrect = true;
			}
		}
		return isCorrect;
	}
}
