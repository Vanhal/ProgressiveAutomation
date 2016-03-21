package com.vanhal.progressiveautomation.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDictionary extends Slot {
	protected int id;
	
	public SlotDictionary(ItemStack itemStack, IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
		//TODO: Rewrite
		//id = OreDictionary.getOreID(itemStack);
	}

	public boolean isItemValid(ItemStack itemStack) {
		return false;
		//return (id == OreDictionary.getOreID(itemStack));
	}
}
