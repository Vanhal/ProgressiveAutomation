package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.item.ItemStack;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;

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
