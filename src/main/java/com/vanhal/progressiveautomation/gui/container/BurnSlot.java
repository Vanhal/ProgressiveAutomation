package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BurnSlot extends Slot {

	public BurnSlot(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack) {
		return (itemStack.getItem()==Item.getItemFromBlock(Blocks.cobblestone));
	}
}
