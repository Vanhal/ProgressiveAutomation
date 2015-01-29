package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.items.tools.ItemWitherDiamond;
import com.vanhal.progressiveautomation.items.tools.ItemWitherIron;
import com.vanhal.progressiveautomation.items.tools.ItemWitherStone;
import com.vanhal.progressiveautomation.items.tools.ItemWitherWood;
import com.vanhal.progressiveautomation.items.tools.WitherTools;
import com.vanhal.progressiveautomation.items.upgrades.ItemCobbleGenUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemDiamondUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFillerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemIronUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemStoneUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWitherUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWoodUpgrade;

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
		}
		CheatRFEngine = new ItemCreativeRFEngine();

		//preInit them
		Item previousTier = Items.redstone;
		if (woodUpgrade!=null){
			woodUpgrade.preInit(previousTier);
			previousTier = woodUpgrade;
		}
		if (stoneUpgrade!=null) {
			stoneUpgrade.preInit(previousTier);
			previousTier = stoneUpgrade;
		}
		if (ironUpgrade!=null) {
			ironUpgrade.preInit(previousTier);
			previousTier = ironUpgrade;
		}
		if (diamondUpgrade!=null) {
			diamondUpgrade.preInit(previousTier);
		}
		
		if (witherUpgrade!=null) witherUpgrade.preInit();
		if (fillerUpgrade!=null) fillerUpgrade.preInit();

		if (PAConfig.rfSupport) {
			rfEngine.preInit();
		}
		CheatRFEngine.preInit();

		if (coalPellet!=null) coalPellet.preInit();
		
		//Initialise the WitherTools
		if (PAConfig.enableWitherTools) {
			WitherTools.preInit();
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
	


}
