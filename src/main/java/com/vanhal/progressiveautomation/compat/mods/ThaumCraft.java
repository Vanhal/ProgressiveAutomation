package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.item.ItemStack;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class ThaumCraft extends BaseMod {
	
	public ThaumCraft() {
		modID = "Thaumcraft";
	}
	
	@Override
	public boolean isLog(ItemStack item) {
		if (item == null) return false;
		// the item may be hiding in a delegate, so use getItem()
		if (item.getItem() == null) return false;
		return (item.getItem().getUnlocalizedName().contains("tile.blockMagicalLog"));
	}
	
	@Override
	public boolean isLeaf(ItemStack item) {
		if (item == null) return false;
		// the item may be hiding in a delegate, so use getItem()
		if (item.getItem() == null) return false;
		return (item.getItem().getUnlocalizedName().contains("tile.blockMagicalLog"));
	}	
}
