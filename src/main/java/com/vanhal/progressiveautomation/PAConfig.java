package com.vanhal.progressiveautomation;

import com.vanhal.progressiveautomation.util.ConfigHandler;

public class PAConfig {
	public static ConfigHandler config;
	
	//config options
	public static int upgradeRange;
	public static int fuelCost;
	
	//blocks
	public static boolean minerEnabled;
	public static boolean chopperEnabled;
	public static boolean generatorEnabled;
	
	//allow levels
	public static boolean allowWoodenLevel;
	public static boolean allowStoneLevel;
	public static boolean allowIronLevel;
	public static boolean allowDiamondLevel;
	
	//rf options
	public static boolean rfSupport;
	public static int rfCost;
	public static int rfStored;
	public static int rfRate;
	public static boolean enableGenerator;
	
	
	public static void init(ConfigHandler handle) {
		config = handle;
		upgradeRange = config.get("upgrades", "UpdateRange", 1, "How many blocks does each upgrade add (default is 1)");
		fuelCost = config.get("general", "fuelCost", 2, "Number to divide the normal burn time by for all machines.");
		if (fuelCost<=0) fuelCost = 1;
		
		//rf options
		rfSupport = config.get("rfoptions", "enableRF", true, "Set to false to disable RF support in this mod");
		
		rfCost = config.get("rfoptions", "rfCost", 40, "RF per tick that the machines use");
		if (rfCost<=0) rfCost = 1000; //Cheater! Take that! :P
		
		rfStored = config.get("rfoptions", "rfStored", 40000, "Amount of RF that the Engines store, needs to be at least the same as the cost");
		if (rfStored<rfCost) rfStored = rfCost;
		
		rfRate = config.get("rfoptions", "rfRate", 1000, "The max rate at which RF can flow into the machines");
		if (rfRate<=0) rfRate = 1000;
		
		enableGenerator = config.get("rfoptions", "generator", true, "Allows the PA generator to be made");
		
		//enable blocks
		minerEnabled = config.get("blocks", "miner", true, "Miner Block is enabled");
		chopperEnabled = config.get("blocks", "chopper", true, "Tree Chopper Block is enabled");
		generatorEnabled = config.get("blocks", "chopper", true, "Generator Block is enabled");
		
		allowWoodenLevel = config.get("upgrades", "wooden", true, "Allow wooden level blocks");
		allowStoneLevel = config.get("upgrades", "stone", true, "Allow stone level blocks");
		allowIronLevel = config.get("upgrades", "iron", true, "Allow iron level blocks");
		allowDiamondLevel = config.get("upgrades", "diamond", true, "Allow diamond level blocks");
	}
}
