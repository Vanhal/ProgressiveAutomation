package com.vanhal.progressiveautomation.ref;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class ToolInfo {
	//tools
	public static int TYPE_PICKAXE = 0;
	public static int TYPE_SHOVEL = 1;
	public static int TYPE_AXE = 2;
	public static int TYPE_SWORD = 3;
	public static int TYPE_HOE = 4;
	
	//levels
	public static int LEVEL_WOOD = 0;
	public static int LEVEL_STONE = 1;
	public static int LEVEL_IRON = 2;
	public static int LEVEL_GOLD = 3;
	public static int LEVEL_DIAMOND = 4;
	
	public static int getType(Item item) {
		if (item instanceof ItemPickaxe) {
			return TYPE_PICKAXE;
		} else if (item instanceof ItemAxe) {
			return TYPE_AXE;
		} else if (item instanceof ItemSpade) {
			return TYPE_SHOVEL;
		} else if (item instanceof ItemSword) {
			return TYPE_SWORD;
		} else if (item instanceof ItemHoe) {
			return TYPE_HOE;
		} else {
			return -1;
		}
	}
	
	public static int getLevel(Item item) {
		String material = "";
		if (item instanceof ItemTool) material = ((ItemTool)item).getToolMaterialName();
		if (item instanceof ItemSword) material = ((ItemSword)item).getToolMaterialName();
		if (item instanceof ItemHoe) material = ((ItemHoe)item).getToolMaterialName();
		
		//ProgressiveAutomation.logger.info("Level: "+material);
		
		if (material.equals("WOOD")) return LEVEL_WOOD;
		else if (material.equals("STONE")) return LEVEL_STONE;
		else if (material.equals("IRON")) return LEVEL_IRON;
		else if (material.equals("GOLD")) return LEVEL_GOLD;
		else if (material.equals("EMERALD")) return LEVEL_DIAMOND;
		else return -1;
	}
	
	public static int getHarvestLevel(Item item) {
		int value = getLevel(item);
		if (value == LEVEL_GOLD) value = 0;
		if (value == LEVEL_DIAMOND) value = 3;
		return value;
	}
}
