package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.item.ItemStack;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;

public class ThaumCraft extends BaseMod {
	
	@Override
	public boolean shouldLoad() {
		ProgressiveAutomation.logger.info("Thaumcraft Loaded");
		return true;
	}
	
	@Override
	public boolean isLog(ItemStack item) {
		return (item.getUnlocalizedName().contains("tile.blockMagicalLog"));
	}
	
	@Override
	public boolean isLeaf(ItemStack item) {
		return (item.getUnlocalizedName().contains("tile.blockMagicalLeaves"));
	}
	
}
