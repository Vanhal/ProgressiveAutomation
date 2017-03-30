package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.item.ItemStack;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;

public class IC2 extends BaseMod  {
	public IC2() {
		modID = "IC2";
	}
	
	public boolean isLog(ItemStack item) {
		if (item == null) return false;
		// the item may be hiding in a delegate, so use getItem()
		if (item.getItem() == null) return false;
		return (item.getItem().getUnlocalizedName().contains("ic2.rubber_wood"));
	}
}
