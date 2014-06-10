package com.vanhal.progressiveautomation;

import com.vanhal.progressiveautomation.util.ConfigHandler;

public class PAConfig {
	public static ConfigHandler config;
	
	//config options
	public static int upgradeRange;
	public static int fuelCost;
	public static int leafTime;
	
	//blocks
	public static boolean minerEnabled;
	public static boolean chopperEnabled;
	
	//allow levels
	public static boolean allowWoodenLevel;
	public static boolean allowStoneLevel;
	public static boolean allowIronLevel;
	public static boolean allowDiamondLevel;
	
	
	public static void init(ConfigHandler handle) {
		config = handle;
		upgradeRange = config.get("upgrades", "UpdateRange", 1, "How many blocks does each upgrade add (default is 1)");
		fuelCost = config.get("general", "fuelCost", 2, "Number to divide the normal burn time by for all machines.");
		
		minerEnabled = config.get("blocks", "miner", true, "Miner Block is enabled");
		chopperEnabled = config.get("blocks", "chopper", true, "Tree Chopper Block is enabled");
		
		allowWoodenLevel = config.get("upgrades", "wooden", true, "Allow wooden level blocks");
		allowStoneLevel = config.get("upgrades", "stone", true, "Allow stone level blocks");
		allowIronLevel = config.get("upgrades", "iron", true, "Allow iron level blocks");
		allowDiamondLevel = config.get("upgrades", "diamond", true, "Allow diamond level blocks");
	}
}
