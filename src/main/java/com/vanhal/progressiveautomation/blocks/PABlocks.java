package com.vanhal.progressiveautomation.blocks;

public class PABlocks {

	public static void preInit() {
		stoneMiner = new BlockMiner();
		
		stoneMiner.preInit();
	}
	
	public static void init() {
		
		stoneMiner.init();
	}
	
	public static void postInit() {
		
		stoneMiner.postInit();
	}
	
	//blocks
	public static BlockMiner stoneMiner;
}
