package com.vanhal.progressiveautomation.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolHelper;

public class PABlocks {

	public static void preInit() {
		//create the blocks
		for (int i = 0; i <= 3; i++) {
			if (PAConfig.allowLevel(i)) {
				if (PAConfig.minerEnabled) miner.add(new BlockMiner(i));
				if (PAConfig.chopperEnabled) chopper.add(new BlockChopper(i));
				if (PAConfig.planterEnabled) planter.add(new BlockPlanter(i));
				if (PAConfig.generatorEnabled && PAConfig.rfSupport) generator.add(new BlockGenerator(i));
			}
		}

		//preInit
		Block middleBlock = BlockMiner.firstMiddleBlock;
		for (BlockMiner blockMiner : miner) {
			blockMiner.preInit(middleBlock);
			middleBlock = blockMiner;
		}
		
		middleBlock = BlockChopper.firstMiddleBlock;
		for (BlockChopper blockChopper : chopper) {
			blockChopper.preInit(middleBlock);
			middleBlock = blockChopper;
		}
		
		middleBlock = BlockPlanter.firstMiddleBlock;
		for (BlockPlanter blockPlanter : planter) {
			blockPlanter.preInit(middleBlock);
			middleBlock = blockPlanter;
		}
		
		middleBlock = BlockGenerator.firstMiddleBlock;
		for (BlockGenerator blockGenerator : generator) {
			blockGenerator.preInit(middleBlock);
			middleBlock = blockGenerator;
		}
	}

	public static void init() {
		for (BlockMiner blockMiner : miner) {
			blockMiner.init();
		}
		
		for (BlockChopper blockChopper : chopper) {
			blockChopper.init();
		}
		
		for (BlockPlanter blockPlanter : planter) {
			blockPlanter.init();
		}
		
		for (BlockGenerator blockGenerator : generator) {
			blockGenerator.init();
		}
	}

	public static void postInit() {
		for (BlockMiner blockMiner : miner) {
			blockMiner.postInit();
		}
		
		for (BlockChopper blockChopper : chopper) {
			blockChopper.postInit();
		}
		
		for (BlockPlanter blockPlanter : planter) {
			blockPlanter.postInit();
		}
		
		for (BlockGenerator blockGenerator : generator) {
			blockGenerator.postInit();
		}
	}

	//blocks

	//miners
	public static List<BlockMiner> miner = new ArrayList<BlockMiner>(4);

	//choppers
	public static List<BlockChopper> chopper = new ArrayList<BlockChopper>(4);

	//planters
	public static List<BlockPlanter> planter = new ArrayList<BlockPlanter>(4);

	//generators
	public static List<BlockGenerator> generator = new ArrayList<BlockGenerator>(4);

}
