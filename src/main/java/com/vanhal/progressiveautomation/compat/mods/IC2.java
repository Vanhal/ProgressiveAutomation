package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.item.ItemStack;

import com.vanhal.progressiveautomation.compat.BaseMod;

public class IC2 extends BaseMod  {
	public IC2() {
		modID = "ic2";
	}
	
	public boolean isLog(ItemStack item) {
		//the item may be hiding in a delegate, so use getItem()
		if (item.isEmpty()) return false;
		if (item.getItem() == null) return false;
		return (item.getItem().getTranslationKey().contains("ic2.rubber_wood"));
	}
}
