package com.vanhal.progressiveautomation;

import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraftforge.common.config.Configuration;

public class PAConfig {
	public static Configuration config;
	
	//config options
	public static int upgradeRange;
	public static int fuelCost;
	
	//blocks
	public static boolean minerEnabled;
	public static boolean chopperEnabled;
	public static boolean planterEnabled;
	public static boolean generatorEnabled;
	public static boolean crafterEnabled;
	
	//allow levels
	public static boolean allowWoodenLevel;
	public static boolean allowStoneLevel;
	public static boolean allowIronLevel;
	public static boolean allowDiamondLevel;
	
	//special upgrades
	public static boolean allowCobbleUpgrade;
	public static boolean allowFillerUpgrade;
	public static boolean allowWitherUpgrade;
	public static int witherMultiplier;
	
	//rf options
	public static boolean rfSupport;
	public static int rfCost;
	public static int rfStored;
	public static int rfRate;
	
	//misc options
	public static boolean allowCoalPellets;
	public static boolean enableWitherTools;
	
	
	public static void init(Configuration handle) {
		config = handle;
		config.load();
		
		syncConfig();
	}
	
	public static void syncConfig() {
		upgradeRange = config.getInt("UpdateRange", "upgrades", 1, 1, 1000, "How many blocks does each upgrade add (default is 1)");
		fuelCost = config.getInt("fuelCost", "general", 2, 1, 300, "Number to divide the normal burn time by for all machines.");
		if (fuelCost<=0) fuelCost = 1;
		
		//rf options
		rfSupport = config.getBoolean("enableRF", "general", true, "Set to false to disable RF support in this mod");
		
		rfCost = config.getInt("rfCost", "rfoptions", 40, 1, 50000, "RF per tick that the machines use");
		if (rfCost<=0) rfCost = 1000; //Cheater! Take that! :P
		
		rfStored = config.getInt("rfStored", "rfoptions", 40000, rfCost, 100000, "Amount of RF that the Engines store, needs to be at least the same as the cost");
		if (rfStored<rfCost) rfStored = rfCost;
		
		rfRate = config.getInt("rfRate", "rfoptions", 1000, 1, 100000, "The max rate at which RF can flow into the machines");
		if (rfRate<=0) rfRate = 1000;
		
		//misc options 
		allowCoalPellets = config.getBoolean("coalPellets", "general", true, "Allow coal pellets (requires restart)");
		enableWitherTools = config.getBoolean("witherTools", "general", true, "Allow Wither tools and resources to create them");
		
		
		//enable blocks		
		minerEnabled = config.getBoolean("miner", "blocks", true, "Miner Block is enabled (requires restart)");
		chopperEnabled = config.getBoolean("chopper", "blocks", true, "Tree Chopper Block is enabled (requires restart)");
		planterEnabled = config.getBoolean("planter", "blocks", true, "Planter/Harvester Block is enabled (requires restart)");
		generatorEnabled = config.getBoolean("generator", "blocks", true, "Generator Block is enabled (requires restart)");
		crafterEnabled = config.getBoolean("crafter", "blocks", true, "Crafter Block is enabled (requires restart)");

		allowWoodenLevel = config.getBoolean("wooden", "upgrades", true, "Allow wooden level blocks (requires restart)");
		allowStoneLevel = config.getBoolean("stone", "upgrades", true, "Allow stone level blocks (requires restart)");
		allowIronLevel = config.getBoolean("iron", "upgrades", true, "Allow iron level blocks (requires restart)");
		allowDiamondLevel = config.getBoolean("diamond", "upgrades", true, "Allow diamond level blocks (requires restart)");
		
		allowCobbleUpgrade = config.getBoolean("cobblegen", "upgrades", true, "Allow cobble gen upgrade for the miner (requires restart)");
		allowFillerUpgrade = config.getBoolean("filler", "upgrades", true, "Allow filler upgrade for the miner (requires restart)");
		allowWitherUpgrade = config.getBoolean("wither", "upgrades", true, "Allow the wither upgrade (requires restart)");
		witherMultiplier = config.getInt("witherMultiplier", "upgrades", 4, 2, 10, "How much the wither upgrade extends the machines. (How much multiplies the upgrades by)");
		
		//save if changed
		if (config.hasChanged()) save();
	}
	
	public static boolean allowLevel(int level) {
		if (level == ToolHelper.LEVEL_WOOD) return allowWoodenLevel;
		else if (level == ToolHelper.LEVEL_STONE) return allowStoneLevel;
		else if (level == ToolHelper.LEVEL_IRON) return allowIronLevel;
		else if (level == ToolHelper.LEVEL_DIAMOND) return allowDiamondLevel;
		return false;
	}
	
	public static void save() {
		config.save();
	}
	
	public static void postInit() {
		save();
	}
}
