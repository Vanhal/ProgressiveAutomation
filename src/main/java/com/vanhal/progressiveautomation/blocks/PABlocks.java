package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;

public class PABlocks {

	public static void preInit() {
		
		if (PAConfig.minerEnabled && PAConfig.allowWoodenLevel) woodenMiner = new BlockMiner();
		if (PAConfig.minerEnabled && PAConfig.allowStoneLevel) stoneMiner = new BlockMinerStone();
		if (PAConfig.minerEnabled && PAConfig.allowIronLevel) ironMiner = new BlockMinerIron();
		if (PAConfig.minerEnabled && PAConfig.allowDiamondLevel) diamondMiner = new BlockMinerDiamond();
		
		if (woodenMiner!=null) woodenMiner.preInit();
		if (stoneMiner!=null) stoneMiner.preInit();
		if (ironMiner!=null) ironMiner.preInit();
		if (diamondMiner!=null) diamondMiner.preInit();
		
	}
	
	public static void init() {
		
		if (woodenMiner!=null) woodenMiner.init();
		if (stoneMiner!=null) stoneMiner.init();
		if (ironMiner!=null) ironMiner.init();
		if (diamondMiner!=null) diamondMiner.init();
	}
	
	public static void postInit() {
		
		if (woodenMiner!=null) woodenMiner.postInit();
		if (stoneMiner!=null) stoneMiner.postInit();
		if (ironMiner!=null) ironMiner.postInit();
		if (diamondMiner!=null) diamondMiner.postInit();
	}
	
	//blocks
	public static BlockMiner woodenMiner = null;
	public static BlockMiner stoneMiner = null;
	public static BlockMiner ironMiner = null;
	public static BlockMiner diamondMiner = null;
	
	
}
