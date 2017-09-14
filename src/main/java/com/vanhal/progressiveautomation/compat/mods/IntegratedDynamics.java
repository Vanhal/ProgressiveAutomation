package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.item.ItemStack;

import com.vanhal.progressiveautomation.compat.BaseMod;

public class IntegratedDynamics extends BaseMod  {
	public IntegratedDynamics() {
		modID = "integrateddynamics";
	}
	
	public boolean isLog(ItemStack item) {
		//the item may be hiding in a delegate, so use getItem()
		if (item.isEmpty()) return false;
		if (item.getItem() == null) return false;
		return (item.getItem().getUnlocalizedName().contains("integrateddynamics.menril_log"));	//NEED TO CONFIRM THIS NAME
	}
}
