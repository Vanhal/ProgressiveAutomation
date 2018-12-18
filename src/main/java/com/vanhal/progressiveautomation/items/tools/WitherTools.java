package com.vanhal.progressiveautomation.items.tools;

public class WitherTools {

	public static void preInit() {
//		RecipeSorter.register(Ref.MODID+":witherTool", RecipeWitherTool.class, SHAPED, "after:forge:shapedore");
		
		witherWood = new ItemWitherWood();
		witherStone = new ItemWitherStone();
		witherIron = new ItemWitherIron();
		witherGold = new ItemWitherGold();
		witherDiamond = new ItemWitherDiamond();
		
		witherWood.preInit();
		witherStone.preInit();
		witherIron.preInit();
		witherGold.preInit();
		witherDiamond.preInit();
	}
	
	public static void init() {
		witherWood.init();
		witherStone.init();
		witherIron.init();
		witherGold.init();
		witherDiamond.init();
	}

	public static void postInit() {

	}
	
	public static ItemWitherWood witherWood = null;
	public static ItemWitherStone witherStone = null;
	public static ItemWitherIron witherIron = null;
	public static ItemWitherGold witherGold = null;
	public static ItemWitherDiamond witherDiamond = null;
}
