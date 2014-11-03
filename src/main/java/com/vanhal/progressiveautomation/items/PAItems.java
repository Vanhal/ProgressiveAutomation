package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.items.tools.ItemWitherDiamond;
import com.vanhal.progressiveautomation.items.tools.ItemWitherIron;
import com.vanhal.progressiveautomation.items.tools.ItemWitherStone;
import com.vanhal.progressiveautomation.items.tools.ItemWitherWood;

public class PAItems {

	public static void preInit() {
		//create items
		if (PAConfig.allowWoodenLevel) woodUpgrade = new ItemWoodUpgrade();
		if (PAConfig.allowStoneLevel) stoneUpgrade = new ItemStoneUpgrade();
		if (PAConfig.allowIronLevel) ironUpgrade = new ItemIronUpgrade();
		if (PAConfig.allowDiamondLevel) diamondUpgrade = new ItemDiamondUpgrade();
		if ((PAConfig.allowCobbleUpgrade) && (PAConfig.minerEnabled)) cobbleUpgrade = new ItemCobbleGenUpgrade();
		if ((PAConfig.allowFillerUpgrade) && (PAConfig.minerEnabled)) fillerUpgrade = new ItemFillerUpgrade();
		if (PAConfig.allowWitherUpgrade) witherUpgrade = new ItemWitherUpgrade();


		if (PAConfig.allowCoalPellets) coalPellet = new ItemCoalPellet();

		if (PAConfig.rfSupport) {
			rfEngine = new ItemRFEngine();
			CheatRFEngine = new ItemCreativeRFEngine();
		}

		//preInit them
		if (woodUpgrade!=null) woodUpgrade.preInit();
		if (stoneUpgrade!=null) stoneUpgrade.preInit();
		if (ironUpgrade!=null) ironUpgrade.preInit();
		if (diamondUpgrade!=null) diamondUpgrade.preInit();
		if (witherUpgrade!=null) witherUpgrade.preInit();
		if (fillerUpgrade!=null) fillerUpgrade.preInit();

		if (PAConfig.rfSupport) {
			rfEngine.preInit();
			CheatRFEngine.preInit();
		}

		if (coalPellet!=null) coalPellet.preInit();
		
		//deal with the various tools
		//wither resources
		if (PAConfig.enableWitherTools) {
			witherWood = new ItemWitherWood();
			witherStone = new ItemWitherStone();
			witherIron = new ItemWitherIron();
			witherDiamond = new ItemWitherDiamond();
			
			witherWood.preInit();
			witherStone.preInit();
			witherIron.preInit();
			witherDiamond.preInit();
		}
	}

	public static void init() {
		if (cobbleUpgrade!=null) cobbleUpgrade.preInit();
	}

	public static void postInit() {

	}

	//items
	public static ItemWoodUpgrade woodUpgrade = null;
	public static ItemStoneUpgrade stoneUpgrade = null;
	public static ItemIronUpgrade ironUpgrade = null;
	public static ItemDiamondUpgrade diamondUpgrade = null;

	public static ItemCobbleGenUpgrade cobbleUpgrade = null;
	public static ItemWitherUpgrade witherUpgrade = null;
	public static ItemFillerUpgrade fillerUpgrade = null;

	public static ItemRFEngine rfEngine = null;
	public static ItemRFEngine CheatRFEngine = null;

	public static ItemCoalPellet coalPellet = null;
	
	//wither tools and resources
	public static ItemWitherWood witherWood = null;
	public static ItemWitherStone witherStone = null;
	public static ItemWitherIron witherIron = null;
	public static ItemWitherDiamond witherDiamond = null;

}
