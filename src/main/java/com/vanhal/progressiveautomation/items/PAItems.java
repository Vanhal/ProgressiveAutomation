package com.vanhal.progressiveautomation.items;

public class PAItems {

	public static void preInit() {
		//create items
		woodUpgrade = new ItemWoodUpgrade();
		stoneUpgrade = new ItemStoneUpgrade();
		ironUpgrade = new ItemIronUpgrade();
		diamondUpgrade = new ItemDiamondUpgrade();
		
		//preInit them
		woodUpgrade.preInit();
		stoneUpgrade.preInit();
		ironUpgrade.preInit();
		diamondUpgrade.preInit();
	}
	
	public static void init() {
		
	}
	
	public static void postInit() {
		
	}
	
	//items
	public static ItemWoodUpgrade woodUpgrade;
	public static ItemStoneUpgrade stoneUpgrade;
	public static ItemIronUpgrade ironUpgrade;
	public static ItemDiamondUpgrade diamondUpgrade;
	
	
}
