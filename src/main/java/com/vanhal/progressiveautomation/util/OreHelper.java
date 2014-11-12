package com.vanhal.progressiveautomation.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreHelper {

	public static boolean ItemOreMatch(ItemStack item1, ItemStack item2) {
		if (item1.isItemEqual(item2)) {
			return true;
		}
		int[] ores1 = OreDictionary.getOreIDs(item1);
		if (ores1.length==0) return false;
		int[] ores2 = OreDictionary.getOreIDs(item2);
		if (ores2.length==0) return false;
		
		for (int i = 0; i < ores1.length; i++) {
			for (int j = 0; j < ores2.length; j++) {
				if (ores1[i] == ores2[j]) return true;
			}
		}
		return false;
	}
}
