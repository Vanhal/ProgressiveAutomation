package com.vanhal.progressiveautomation.compat.mods;

import com.vanhal.progressiveautomation.compat.BaseMod;

import net.minecraft.item.ItemStack;

public class ThaumCraft extends BaseMod {
	
	public ThaumCraft() {
		modID = "Thaumcraft";
	}
	
	@Override
	public boolean isLog(ItemStack item) {
		if (item == null) return false;
		return (item.getUnlocalizedName().contains("tile.blockMagicalLog"));
	}
	
	@Override
	public boolean isLeaf(ItemStack item) {
		if (item == null) return false;
		return (item.getUnlocalizedName().contains("tile.blockMagicalLeaves"));
	}
	
}
