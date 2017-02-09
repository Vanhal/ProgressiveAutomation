package com.vanhal.progressiveautomation.compat.mods;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;
import com.vanhal.progressiveautomation.util.OreHelper;

import net.minecraft.block.BlockLeaves;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class TiCon extends BaseMod {
	
	public TiCon() {
		modID = "tconstruct";
	}
	
	@Override
	public boolean shouldLoad() {
		ProgressiveAutomation.logger.info("TiCon Loaded");
		return true;
	}
	
	@Override
	public boolean isLog(ItemStack stack) {
		if (OreHelper.testOre("blockSlimeCongealed", stack)) {
			return true;
		}
		return false;
	}

}
