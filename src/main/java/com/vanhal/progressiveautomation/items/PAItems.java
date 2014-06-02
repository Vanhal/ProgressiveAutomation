package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.PAConfig;

public class PAItems {

	public static void preInit() {
		//create items
		if (PAConfig.allowWoodenLevel) woodUpgrade = new ItemWoodUpgrade();
		if (PAConfig.allowStoneLevel) stoneUpgrade = new ItemStoneUpgrade();
		if (PAConfig.allowIronLevel) ironUpgrade = new ItemIronUpgrade();
		if (PAConfig.allowDiamondLevel) diamondUpgrade = new ItemDiamondUpgrade();
		
		//preInit them
		if (woodUpgrade!=null) woodUpgrade.preInit();
		if (stoneUpgrade!=null) stoneUpgrade.preInit();
		if (ironUpgrade!=null) ironUpgrade.preInit();
		if (diamondUpgrade!=null) diamondUpgrade.preInit();
	}
	
	public static void init() {
		
	}
	
	public static void postInit() {
		
	}
	
	//items
	public static ItemWoodUpgrade woodUpgrade = null;
	public static ItemStoneUpgrade stoneUpgrade = null;
	public static ItemIronUpgrade ironUpgrade = null;
	public static ItemDiamondUpgrade diamondUpgrade = null;
	
	
}
