package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.entities.planter.TilePlanter;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPlantable  extends Slot {
	
	public SlotPlantable(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack) {
		//ProgressiveAutomation.logger.info("Planter: "+itemStack.getUnlocalizedName());
		return TilePlanter.isPlantable(itemStack);
	}
}
