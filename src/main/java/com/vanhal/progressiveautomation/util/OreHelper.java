package com.vanhal.progressiveautomation.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
	
	public static boolean testOreBlock(String testOre, BlockPos testPos, IBlockAccess worldObj) {
		return testOreBlock(OreDictionary.getOreID(testOre), testPos, worldObj);
	}
	
	public static boolean testOreBlock(int testOreID, BlockPos testPos, IBlockAccess worldObj) {
		IBlockState _blockState = worldObj.getBlockState(testPos);
		Block _block = _blockState.getBlock();
		int metaData = _block.getMetaFromState(_blockState);
		
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
