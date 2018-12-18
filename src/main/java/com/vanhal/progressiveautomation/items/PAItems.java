package com.vanhal.progressiveautomation.items;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.blocks.BaseBlock;
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

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PAItems {
	public static final List<Item> items = new ArrayList<>();
	public static Item wrench;
	public static Item cheatRFEngine;
	
	public static void preInit() {
		//create items
		
		
		if (PAConfig.allowWrench) {
			wrench = new ItemWrench();
			items.add(wrench);
		}
		
		if (PAConfig.allowWoodenLevel) items.add(new ItemWoodUpgrade());
		if (PAConfig.allowStoneLevel) items.add(new ItemStoneUpgrade());
		if (PAConfig.allowIronLevel) items.add(new ItemIronUpgrade());
		if (PAConfig.allowDiamondLevel) items.add(new ItemDiamondUpgrade());
		if ((PAConfig.allowCobbleUpgrade) && (PAConfig.minerEnabled)) items.add(new ItemCobbleGenUpgrade());
		if ((PAConfig.allowFillerUpgrade) && (PAConfig.minerEnabled)) items.add(new ItemFillerUpgrade());
		if (PAConfig.allowWitherUpgrade) items.add(new ItemWitherUpgrade());
		
		if (PAConfig.killerEnabled) {
			if (PAConfig.allowKillPlayer) items.add(new ItemFilterPlayerUpgrade());
			items.add(new ItemFilterMobUpgrade());
			items.add(new ItemFilterAnimalUpgrade());
			items.add(new ItemFilterAdultUpgrade());
		}
		
		if (PAConfig.farmerEnabled) {
			if (PAConfig.allowMilkerUpgrade) items.add(new ItemMilkerUpgrade());
			if (PAConfig.allowShearingUpgrade) items.add(new ItemShearingUpgrade());
		}


		if (PAConfig.allowCoalPellets) items.add(new ItemCoalPellet());

		if (PAConfig.rfSupport) {
			items.add(new ItemRFEngine());
		}
		cheatRFEngine = new ItemCreativeRFEngine();
		items.add(cheatRFEngine);

	}

	public static void addItemBlock(BaseBlock baseBlock) {
		ItemBlock nb = new ItemBlock(baseBlock);
		nb.setRegistryName(baseBlock.getRegistryName());
		items.add(nb);
	}
	
	@SubscribeEvent
	public static final void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(items.toArray(new Item[items.size()]));
	}
}
