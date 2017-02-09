package com.vanhal.progressiveautomation;

import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraftforge.common.config.Configuration;

public class PAConfig {
	public static Configuration config;
	
	//config options
	public static int initialRange;
	public static int upgradeRange;
	public static int fuelCost;
	
	//blocks
	public static boolean minerEnabled;
	public static boolean chopperEnabled;
	public static boolean planterEnabled;
	public static boolean generatorEnabled;
	public static boolean crafterEnabled;
	public static boolean farmerEnabled;
	public static boolean killerEnabled;
	public static boolean capacitorEnabled;
	
	//allow levels
	public static boolean allowWoodenLevel;
	public static boolean allowStoneLevel;
	public static boolean allowIronLevel;
	public static boolean allowDiamondLevel;
	
	//special upgrades
	public static boolean allowCobbleUpgrade;
	public static boolean allowShearingUpgrade;
	public static boolean allowMilkerUpgrade;
	public static boolean allowFillerUpgrade;
	public static boolean allowWitherUpgrade;
	public static int witherMultiplier;
	
	//killer kills players?
	public static boolean allowKillPlayer;
	
	//rf options
	public static boolean rfSupport;
	public static int rfCost;
	public static int rfStored;
	public static int rfRate;
	public static int rfStorageFactor;
	
	//misc options
	public static boolean allowCoalPellets;
	public static boolean enableWitherTools;
	public static boolean allowPotatos;
	public static boolean allowWrench;
	public static boolean destroyTools;
	public static boolean shearTrees;
	public static boolean allowInventoryOverflow;
	public static boolean pauseOnFullInventory;
	public static int maxRangeUpgrades;
	
	//machine levels
	public static int woodLevel;
	public static int stoneLevel;
	public static int ironLevel;
	public static int diamondLevel;
	
	
	public static void init(Configuration handle) {
		config = handle;
		config.load();
		
		config.addCustomCategoryComment("general", "General Settings");
		config.addCustomCategoryComment("blocks", "Enable or disable the blocks, will remove them from the game entirely");
		config.addCustomCategoryComment("upgrades", "Change the settings of how the various machine upgrades work");
		config.addCustomCategoryComment("rfoptions", "Change the settings of how PA interacts with RF devices");
		
		config.addCustomCategoryComment("toolLevels", "Here you can adjust the tools that each machine level can take.\n"
				+"Each option takes a mining level (0 being wood, 1 is stone etc)\n"
				+"Normally you would not have to change these options, but if you are using the likes of\n"
				+"Igunia tweaks then it is advised to change these to suit your set up");
		
		config.addCustomCategoryComment("modcompatibility", 
				"This section allows you to customise which mods will be compatible with the various machines\n"
				+ "Please note however that disabling compatibilty does not mean the mod will not work with PA,\n"
				+ "just that it will not work 100% as expected");
		
		
		
		syncConfig();
	}
	
	public static void syncConfig() {
		initialRange = config.getInt("InitialRange", "general", 1, 1, 1000, "The default range of the machines without upgrades (default is 1)");
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
		
		rfStorageFactor = config.getInt("rfStorageFactor", "rfoptions", 4, 1, 8, "The multiplier that is applied to the Capacitor RF storage size");
		if (rfStorageFactor<=0) rfStorageFactor = 1;
		
		//misc options 
		allowCoalPellets = config.getBoolean("coalPellets", "general", true, "Allow coal pellets (requires restart)");
		allowPotatos = config.getBoolean("allowPotatos", "general", true, "Allow Potatos to be used as a fuel source in PA machines");
		enableWitherTools = config.getBoolean("witherTools", "general", true, "Allow Wither tools and resources to create them");
		allowWrench = config.getBoolean("allowWrench", "general", true, "Allows the wrench, you've got to be seriously evil to not allow this!");
		destroyTools = config.getBoolean("destroyTools", "general", true, "Changing to false will make the machines spit a fully broken vanilla tool into it's inventory");
		shearTrees = config.getBoolean("shearTrees", "general", true, "Allow the chopper to take a shearing upgrade in order to have a sheer to shear leaves");
		allowInventoryOverflow = config.getBoolean("allowInventoryOverflow", "general", true, "Drop items on the ground if machine's inventory is full, setting this to false will destroy overflow items.");
		pauseOnFullInventory = config.getBoolean("pauseOnFullInventory",  "general",  false, "Pause machines when the are no open slots in their inventory.");

		
		//enable blocks		
		minerEnabled = config.getBoolean("miner", "blocks", true, "Miner Block is enabled (requires restart)");
		chopperEnabled = config.getBoolean("chopper", "blocks", true, "Tree Chopper Block is enabled (requires restart)");
		planterEnabled = config.getBoolean("planter", "blocks", true, "Planter/Harvester Block is enabled (requires restart)");
		generatorEnabled = config.getBoolean("generator", "blocks", true, "Generator Block is enabled (requires restart)");
		crafterEnabled = config.getBoolean("crafter", "blocks", true, "Crafter Block is enabled (requires restart)");
		farmerEnabled = config.getBoolean("farmer", "blocks", true, "Killer Block is enabled (requires restart)");
		killerEnabled = config.getBoolean("killer", "blocks", true, "Animal Farmer Block is enabled (requires restart)");
		capacitorEnabled = config.getBoolean("capacitor", "blocks", true, "Capacitor Block is enabled (requires restart)");

		allowWoodenLevel = config.getBoolean("wooden", "upgrades", true, "Allow wooden level blocks (requires restart)");
		allowStoneLevel = config.getBoolean("stone", "upgrades", true, "Allow stone level blocks (requires restart)");
		allowIronLevel = config.getBoolean("iron", "upgrades", true, "Allow iron level blocks (requires restart)");
		allowDiamondLevel = config.getBoolean("diamond", "upgrades", true, "Allow diamond level blocks (requires restart)");
		
		allowCobbleUpgrade = config.getBoolean("cobblegen", "upgrades", true, "Allow cobble gen upgrade for the miner (requires restart)");
		allowFillerUpgrade = config.getBoolean("filler", "upgrades", true, "Allow filler upgrade for the miner (requires restart)");
		allowWitherUpgrade = config.getBoolean("wither", "upgrades", true, "Allow the wither upgrade (requires restart)");
		allowShearingUpgrade = config.getBoolean("shearing", "upgrades", true, "Allow the shearing upgrade (requires restart)");
		allowMilkerUpgrade = config.getBoolean("milker", "upgrades", true, "Allow the milker upgrade (requires restart)");
		witherMultiplier = config.getInt("witherMultiplier", "upgrades", 4, 2, 10, "How much the wither upgrade extends the machines. (How much multiplies the upgrades by)");
		
		allowKillPlayer = config.getBoolean("killPlayer", "upgrades", true, "Allow the Killer to kill players");
		
		maxRangeUpgrades = config.getInt("maxRangeUpgrades", "upgrades", Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "Max amount of range upgrades that can be put into a machine");
		
		//toolLevels
		woodLevel = config.getInt("woodLevel", "toolLevels", ToolHelper.LEVEL_WOOD, 0, 100, "The max mining level of the tool that wooden machines will take");
		stoneLevel = config.getInt("stoneLevel", "toolLevels", ToolHelper.LEVEL_STONE, 0, 100, "The max mining level of the tool that stone machines will take");
		ironLevel = config.getInt("ironLevel", "toolLevels", ToolHelper.LEVEL_IRON, 0, 100, "The max mining level of the tool that iron machines will take");
		diamondLevel = config.getInt("diamondLevel", "toolLevels", ToolHelper.LEVEL_MAX, 0, 100, "The max mining level of the tool that diamond machines will take");
		
		
		//save if changed
		if (config.hasChanged()) save();
	}
	
	public static int getToolConfigLevel(int level) {
		if (level == ToolHelper.LEVEL_WOOD) return woodLevel;
		else if (level == ToolHelper.LEVEL_STONE) return stoneLevel;
		else if (level == ToolHelper.LEVEL_IRON) return ironLevel;
		else if (level == ToolHelper.LEVEL_DIAMOND) return diamondLevel;
		return level;
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
