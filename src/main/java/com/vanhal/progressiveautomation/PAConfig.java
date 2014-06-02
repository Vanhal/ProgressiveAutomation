package com.vanhal.progressiveautomation;

import com.vanhal.progressiveautomation.util.ConfigHandler;

public class PAConfig {
	public static ConfigHandler config;
	
	//config options
	public static int upgradeRange;
	
	//blocks
	public static boolean minerEnabled;
	
	//allow levels
	public static boolean allowWoodenLevel;
	public static boolean allowStoneLevel;
	public static boolean allowIronLevel;
	public static boolean allowDiamondLevel;
	
	
	public static void init(ConfigHandler handle) {
		config = handle;
		upgradeRange = config.get("upgrades", "UpdateRange", 1, "How many blocks does each upgrade add (default is 1)");
		
		minerEnabled = config.get("blocks", "miner", true, "Miner Block is enabled");
		
		allowWoodenLevel = config.get("upgrades", "wooden", true, "Allow wooden level blocks");
		allowStoneLevel = config.get("upgrades", "stone", true, "Allow stone level blocks");
		allowIronLevel = config.get("upgrades", "iron", true, "Allow iron level blocks");
		allowDiamondLevel = config.get("upgrades", "diamond", true, "Allow diamond level blocks");
	}
}
