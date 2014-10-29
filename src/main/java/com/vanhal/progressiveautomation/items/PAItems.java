package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.vanhal.progressiveautomation.PAConfig;

public class PAItems {

	public static void preInit() {
		//create items
		if (PAConfig.allowWoodenLevel) woodUpgrade = new ItemWoodUpgrade();
		if (PAConfig.allowStoneLevel) stoneUpgrade = new ItemStoneUpgrade();
		if (PAConfig.allowIronLevel) ironUpgrade = new ItemIronUpgrade();
		if (PAConfig.allowDiamondLevel) diamondUpgrade = new ItemDiamondUpgrade();
		if ((PAConfig.allowCobbleUpgrade) && (PAConfig.minerEnabled)) cobbleUpgrade = new ItemCobbleGenUpgrade();
		if (PAConfig.allowWitherUpgrade) witherUpgrade = new ItemWitherUpgrade();


		if (PAConfig.allowCoalPellets) coalPellet = new ItemCoalPellet();

		if (PAConfig.rfSupport) {
			rfEngine = new ItemRFEngine();
			CheatRFEngine = new ItemCreativeRFEngine();
		}

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
		if (witherUpgrade!=null) witherUpgrade.preInit(null);

		if (PAConfig.rfSupport) {
			rfEngine.preInit();
			CheatRFEngine.preInit();
		}

		if (coalPellet!=null) coalPellet.preInit();
	}

	public static void init() {
		if (cobbleUpgrade!=null) cobbleUpgrade.preInit(null);
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

	public static ItemRFEngine rfEngine = null;
	public static ItemRFEngine CheatRFEngine = null;

	public static ItemCoalPellet coalPellet = null;

}
