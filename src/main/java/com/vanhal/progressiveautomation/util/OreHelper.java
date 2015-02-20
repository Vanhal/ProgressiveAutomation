package com.vanhal.progressiveautomation.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
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
	
	public static boolean testOreBlock(String testOre, int x, int y, int z, IBlockAccess worldObj) {
		return testOreBlock(OreDictionary.getOreID(testOre), x, y, z, worldObj);
	}
	
	public static boolean testOreBlock(int testOreID, int x, int y, int z, IBlockAccess worldObj) {
		Block _block = worldObj.getBlock(x, y, z);
		int metaData = worldObj.getBlockMetadata(x, y, z);
		
		ItemStack testItem = new ItemStack(Item.getItemFromBlock(_block), 1, metaData);
		return testOre(testOreID, testItem);
	}
	
	
	public static boolean testOre(String testOre, ItemStack testItem) {
		return testOre(OreDictionary.getOreID(testOre), testItem);
	}
	
	public static boolean testOre(int testOreID, ItemStack testItem) {
		if (testItem == null || testItem.getItem() == null) return false;
		int[] ordIDs = OreDictionary.getOreIDs(testItem);
		for (int oreID: ordIDs) {
			if (testOreID == oreID) return true;
		}
		return false;
	}
}
