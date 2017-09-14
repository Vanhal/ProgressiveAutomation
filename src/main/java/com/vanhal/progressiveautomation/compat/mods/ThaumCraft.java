package com.vanhal.progressiveautomation.compat.mods;

import com.vanhal.progressiveautomation.compat.BaseMod;

import net.minecraft.item.ItemStack;

public class ThaumCraft extends BaseMod {
	
	public ThaumCraft() {
		modID = "thaumcraft";
	}
	
	@Override
	public boolean isLog(ItemStack item) {
		if (item.isEmpty()) return false;
		// the item may be hiding in a delegate, so use getItem()
		if (item.getItem() == null) return false;
		return (item.getItem().getUnlocalizedName().contains("tile.blockMagicalLog"));
	}
	
	@Override
	public boolean isLeaf(ItemStack item) {
		if (item.isEmpty()) return false;
		// the item may be hiding in a delegate, so use getItem()
		if (item.getItem() == null) return false;
		return (item.getItem().getUnlocalizedName().contains("tile.blockMagicalLog"));
	}	
}
