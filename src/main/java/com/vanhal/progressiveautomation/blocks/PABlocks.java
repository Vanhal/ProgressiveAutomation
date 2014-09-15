package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolHelper;

public class PABlocks {

	public static void preInit() {
		//create the blocks
		for (int i = 0; i <= 3; i++) {
			if (PAConfig.allowLevel(i)) {
				if (PAConfig.minerEnabled) miner[i] = new BlockMiner(i);
				if (PAConfig.chopperEnabled) chopper[i] = new BlockChopper(i);
				if (PAConfig.planterEnabled) planter[i] = new BlockPlanter(i);
				if (PAConfig.generatorEnabled && PAConfig.rfSupport) generator[i] = new BlockGenerator(i);
			}
		}

		//preInit
		for (int i = 0; i <= 3; i++) {
			if (PAConfig.allowLevel(i)) {
				if (PAConfig.minerEnabled) miner[i].preInit();
				if (PAConfig.chopperEnabled)chopper[i].preInit();
				if (PAConfig.planterEnabled) planter[i].preInit();
				if (PAConfig.generatorEnabled && PAConfig.rfSupport) generator[i].preInit();
			}
		}
	}

	public static void init() {
		for (int i = 0; i <= 3; i++) {
			if (PAConfig.allowLevel(i)) {
				if (PAConfig.minerEnabled) miner[i].init();
				if (PAConfig.chopperEnabled)chopper[i].init();
				if (PAConfig.planterEnabled) planter[i].init();
				if (PAConfig.generatorEnabled && PAConfig.rfSupport) generator[i].init();
			}
		}
	}

	public static void postInit() {
		for (int i = 0; i <= 3; i++) {
			if (PAConfig.allowLevel(i)) {
				if (PAConfig.minerEnabled) miner[i].postInit();
				if (PAConfig.chopperEnabled)chopper[i].postInit();
				if (PAConfig.planterEnabled) planter[i].postInit();
				if (PAConfig.generatorEnabled && PAConfig.rfSupport) generator[i].postInit();
			}
		}
	}

	//blocks

	//miners
	public static BlockMiner[] miner = new BlockMiner[4];

	//choppers
	public static BlockChopper[] chopper = new BlockChopper[4];

	//planters
	public static BlockPlanter[] planter = new BlockPlanter[4];

	//generators
	public static BlockGenerator[] generator = new BlockGenerator[4];

}
