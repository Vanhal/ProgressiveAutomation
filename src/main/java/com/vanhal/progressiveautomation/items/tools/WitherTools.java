package com.vanhal.progressiveautomation.items.tools;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WitherTools {

	public static void preInit() {
		witherWood = new ItemWitherWood();
		witherStone = new ItemWitherStone();
		witherIron = new ItemWitherIron();
		witherGold = new ItemWitherGold();
		witherDiamond = new ItemWitherDiamond();
		
	}
	
	public static ItemWitherWood witherWood = null;
	public static ItemWitherStone witherStone = null;
	public static ItemWitherIron witherIron = null;
	public static ItemWitherGold witherGold = null;
	public static ItemWitherDiamond witherDiamond = null;
	
	@SubscribeEvent
	public static void registerBits(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(witherWood, witherStone, witherIron, witherGold, witherDiamond);
	}
}
