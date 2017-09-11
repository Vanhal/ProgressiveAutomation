package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.items.tools.WitherTools;
import com.vanhal.progressiveautomation.items.upgrades.ItemCobbleGenUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemDiamondUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFillerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterAdultUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterAnimalUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterMobUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterPlayerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemIronUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemMilkerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemShearingUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemStoneUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWitherUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWoodUpgrade;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class PAItems {

	public static void preInit() {
		//create items
		
		
		if (PAConfig.allowWrench) {
			wrench = new ItemWrench();
			wrench.preInit();
		}
		
		if (PAConfig.allowWoodenLevel) woodUpgrade = new ItemWoodUpgrade();
		if (PAConfig.allowStoneLevel) stoneUpgrade = new ItemStoneUpgrade();
		if (PAConfig.allowIronLevel) ironUpgrade = new ItemIronUpgrade();
		if (PAConfig.allowDiamondLevel) diamondUpgrade = new ItemDiamondUpgrade();
		if ((PAConfig.allowCobbleUpgrade) && (PAConfig.minerEnabled)) cobbleUpgrade = new ItemCobbleGenUpgrade();
		if ((PAConfig.allowFillerUpgrade) && (PAConfig.minerEnabled)) fillerUpgrade = new ItemFillerUpgrade();
		if (PAConfig.allowWitherUpgrade) witherUpgrade = new ItemWitherUpgrade();
		
		if (PAConfig.killerEnabled) {
			if (PAConfig.allowKillPlayer) filterPlayerUpgrade = new ItemFilterPlayerUpgrade();
			filterMobUpgrade = new ItemFilterMobUpgrade();
			filterAnimalUpgrade = new ItemFilterAnimalUpgrade();
			filterAdultUpgrade = new ItemFilterAdultUpgrade();
		}
		
		if (PAConfig.farmerEnabled) {
			if (PAConfig.allowMilkerUpgrade) milkerUpgrade = new ItemMilkerUpgrade();
			if (PAConfig.allowShearingUpgrade) shearingUpgrade = new ItemShearingUpgrade();
		}


		if (PAConfig.allowCoalPellets) coalPellet = new ItemCoalPellet();

		if (PAConfig.rfSupport) {
			rfEngine = new ItemRFEngine();
		}
		cheatRFEngine = new ItemCreativeRFEngine();

		//preInit them
		Item previousTier = Items.REDSTONE;
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
		
		if (filterPlayerUpgrade!=null) filterPlayerUpgrade.preInit();
		if (filterMobUpgrade!=null) filterMobUpgrade.preInit();
		if (filterAnimalUpgrade!=null) filterAnimalUpgrade.preInit();
		if (filterAdultUpgrade!=null) filterAdultUpgrade.preInit();
		
		if (milkerUpgrade!=null) milkerUpgrade.preInit();
		if (shearingUpgrade!=null) shearingUpgrade.preInit();

		if (PAConfig.rfSupport) {
			rfEngine.preInit();
		}
		cheatRFEngine.preInit();

		if (coalPellet!=null) coalPellet.preInit();
		
		//Initialise the WitherTools
		if (PAConfig.enableWitherTools) {
			WitherTools.preInit();
		}
	}

	public static void init(FMLInitializationEvent event) {
		if (cobbleUpgrade!=null) cobbleUpgrade.preInit();
		
		if (event.getSide() == Side.CLIENT) {
			if (wrench!=null) wrench.init();
			
			if (woodUpgrade!=null) woodUpgrade.init();
			if (stoneUpgrade!=null) stoneUpgrade.init();
			if (ironUpgrade!=null) ironUpgrade.init();
			if (diamondUpgrade!=null) diamondUpgrade.init();
			if (witherUpgrade!=null) witherUpgrade.init();
			if (cobbleUpgrade!=null) cobbleUpgrade.init();
			if (fillerUpgrade!=null) fillerUpgrade.init();
			if (milkerUpgrade!=null) milkerUpgrade.init();
			if (shearingUpgrade!=null) shearingUpgrade.init();
			
			if (filterMobUpgrade!=null) filterMobUpgrade.init();
			if (filterAnimalUpgrade!=null) filterAnimalUpgrade.init();
			if (filterAdultUpgrade!=null) filterAdultUpgrade.init();
			if (filterPlayerUpgrade!=null) filterPlayerUpgrade.init();
			
			
			if (rfEngine!=null) rfEngine.init();
			cheatRFEngine.init();
			
			if (coalPellet!=null) coalPellet.init();
			
			if (PAConfig.enableWitherTools) {
				WitherTools.init();
			}
		}
	}

	public static void postInit() {

	}

	//items
	public static ItemWrench wrench = null;
	public static ItemManual manual = null;
	
	public static ItemWoodUpgrade woodUpgrade = null;
	public static ItemStoneUpgrade stoneUpgrade = null;
	public static ItemIronUpgrade ironUpgrade = null;
	public static ItemDiamondUpgrade diamondUpgrade = null;

	public static ItemCobbleGenUpgrade cobbleUpgrade = null;
	public static ItemWitherUpgrade witherUpgrade = null;
	public static ItemFillerUpgrade fillerUpgrade = null;
	public static ItemMilkerUpgrade milkerUpgrade = null;
	public static ItemShearingUpgrade shearingUpgrade = null;
	
	public static ItemFilterMobUpgrade filterMobUpgrade = null;
	public static ItemFilterAnimalUpgrade filterAnimalUpgrade = null;
	public static ItemFilterAdultUpgrade filterAdultUpgrade = null;
	public static ItemFilterPlayerUpgrade filterPlayerUpgrade = null;

	public static ItemRFEngine rfEngine = null;
	public static ItemRFEngine cheatRFEngine = null;

	public static ItemCoalPellet coalPellet = null;
	


}
