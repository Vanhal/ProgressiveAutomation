package com.vanhal.progressiveautomation.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

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
import com.vanhal.progressiveautomation.ref.Ref;

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
		cheatRFEngine = new ItemCreativeRFEngine();

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
			if (woodUpgrade!=null) woodUpgrade.init();
			if (stoneUpgrade!=null) stoneUpgrade.init();
			if (ironUpgrade!=null) ironUpgrade.init();
			if (diamondUpgrade!=null) diamondUpgrade.init();
			if (witherUpgrade!=null) witherUpgrade.init();
			if (cobbleUpgrade!=null) cobbleUpgrade.init();
			if (fillerUpgrade!=null) fillerUpgrade.init();
			
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
	public static ItemWoodUpgrade woodUpgrade = null;
	public static ItemStoneUpgrade stoneUpgrade = null;
	public static ItemIronUpgrade ironUpgrade = null;
	public static ItemDiamondUpgrade diamondUpgrade = null;

	public static ItemCobbleGenUpgrade cobbleUpgrade = null;
	public static ItemWitherUpgrade witherUpgrade = null;
	public static ItemFillerUpgrade fillerUpgrade = null;

	public static ItemRFEngine rfEngine = null;
	public static ItemRFEngine cheatRFEngine = null;

	public static ItemCoalPellet coalPellet = null;
	


}
