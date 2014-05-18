package com.vanhal.progressiveautomation.blocks;

public class PABlocks {

	public static void preInit() {
		woodenMiner = new BlockMiner();
		stoneMiner = new BlockMinerStone();
		ironMiner = new BlockMinerIron();
		diamondMiner = new BlockMinerDiamond();
		
		woodenMiner.preInit();
		stoneMiner.preInit();
		ironMiner.preInit();
		diamondMiner.preInit();
	}
	
	public static void init() {
		
		woodenMiner.init();
		stoneMiner.init();
		ironMiner.init();
		diamondMiner.init();
	}
	
	public static void postInit() {
		
		woodenMiner.postInit();
		stoneMiner.postInit();
		ironMiner.postInit();
		diamondMiner.postInit();
	}
	
	//blocks
	public static BlockMiner woodenMiner;
	public static BlockMiner stoneMiner;
	public static BlockMiner ironMiner;
	public static BlockMiner diamondMiner;
}
