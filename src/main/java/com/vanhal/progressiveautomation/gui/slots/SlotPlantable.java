package com.vanhal.progressiveautomation.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

public class SlotPlantable  extends Slot {
	
	public SlotPlantable(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack) {
		return ( (itemStack.getItem() instanceof IPlantable) && (OreDictionary.getOreID(itemStack) != OreDictionary.getOreID("treeSapling")) );
	}
}
