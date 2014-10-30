package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SlotDictionary extends Slot {
	protected int id;
	
	public SlotDictionary(ItemStack itemStack, IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
		id = OreDictionary.getOreID(itemStack);
	}

	public boolean isItemValid(ItemStack itemStack) {
		ProgressiveAutomation.logger.info("Name: "+itemStack.getUnlocalizedName());
		ProgressiveAutomation.logger.info("Dic: "+OreDictionary.getOreName(OreDictionary.getOreID(itemStack)));
		return (id == OreDictionary.getOreID(itemStack));
	}
}
