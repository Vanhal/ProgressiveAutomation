package com.vanhal.progressiveautomation.ref;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
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
		if (item instanceof ItemTool) {
			
		} else if (item instanceof ItemSword) {
			return TYPE_SWORD;
		} else if (item instanceof ItemHoe) {
			return TYPE_HOE;
		}
	}
}
