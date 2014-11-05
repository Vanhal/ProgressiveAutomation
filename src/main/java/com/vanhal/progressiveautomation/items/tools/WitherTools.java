package com.vanhal.progressiveautomation.items.tools;

public class WitherTools {

	public static void preInit() {
		witherWood = new ItemWitherWood();
		witherStone = new ItemWitherStone();
		witherIron = new ItemWitherIron();
		witherDiamond = new ItemWitherDiamond();
		
		witherWood.preInit();
		witherStone.preInit();
		witherIron.preInit();
		witherDiamond.preInit();
	}
	
	public static void init() {
		
	}

	public static void postInit() {

	}
	
	//wither resources
	public static ItemWitherWood witherWood = null;
	public static ItemWitherStone witherStone = null;
	public static ItemWitherIron witherIron = null;
	public static ItemWitherDiamond witherDiamond = null;
	
	//wither tools
	//wooden
	
}
