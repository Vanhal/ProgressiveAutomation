package com.vanhal.progressiveautomation.blocks;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;

import net.minecraft.block.Block;

public class PABlocks {

	public static void preInit() {
		//create the blocks
		for (int i = 0; i <= 3; i++) {
			if (PAConfig.allowLevel(i)) {
				if (PAConfig.minerEnabled) miner.add(new BlockMiner(i));
				if (PAConfig.chopperEnabled) chopper.add(new BlockChopper(i));
				if (PAConfig.planterEnabled) planter.add(new BlockPlanter(i));
				if (PAConfig.crafterEnabled) crafter.add(new BlockCrafter(i));
				if (PAConfig.killerEnabled) killer.add(new BlockKiller(i));
				if (PAConfig.farmerEnabled) farmer.add(new BlockFarmer(i));
				if (PAConfig.generatorEnabled && PAConfig.rfSupport) generator.add(new BlockGenerator(i));
				if (PAConfig.capacitorEnabled && PAConfig.rfSupport) capacitor.add(new BlockCapacitor(i));
			}
		}

		//preInit
		Block previousTier = BlockMiner.firstTier;
		for (BlockMiner blockMiner : miner) {
			blockMiner.preInit(previousTier);
			previousTier = blockMiner;
		}
		
		previousTier = BlockChopper.firstTier;
		for (BlockChopper blockChopper : chopper) {
			blockChopper.preInit(previousTier);
			previousTier = blockChopper;
		}
		
		previousTier = BlockPlanter.firstTier;
		for (BlockPlanter blockPlanter : planter) {
			blockPlanter.preInit(previousTier);
			previousTier = blockPlanter;
		}
		
		previousTier = BlockGenerator.firstTier;
		for (BlockGenerator blockGenerator : generator) {
			blockGenerator.preInit(previousTier);
			previousTier = blockGenerator;
		}
		
		previousTier = BlockCrafter.firstTier;
		for (BlockCrafter blockCrafter : crafter) {
			blockCrafter.preInit(previousTier);
			previousTier = blockCrafter;
		}
		
		previousTier = BlockFarmer.firstTier;
		for (BlockFarmer blockFarmer : farmer) {
			blockFarmer.preInit(previousTier);
			previousTier = blockFarmer;
		}
		
		previousTier = BlockKiller.firstTier;
		for (BlockKiller blockKiller : killer) {
			blockKiller.preInit(previousTier);
			previousTier = blockKiller;
		}
		
		previousTier = BlockCapacitor.firstTier;
		for (BlockCapacitor blockCapacitor : capacitor) {
			blockCapacitor.preInit(previousTier);
			previousTier = blockCapacitor;
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
		
		for (BlockCrafter blockCrafter : crafter) {
			blockCrafter.init();
		}
		
		for (BlockKiller blockKiller : killer) {
			blockKiller.init();
		}
		
		for (BlockFarmer blockfarmer : farmer) {
			blockfarmer.init();
		}
		
		for (BlockCapacitor blockCapacitor : capacitor) {
			blockCapacitor.init();
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
		
		for (BlockCrafter blockCrafter : crafter) {
			blockCrafter.postInit();
		}
		
		for (BlockKiller blockKiller : killer) {
			blockKiller.postInit();
		}
		
		for (BlockFarmer blockfarmer : farmer) {
			blockfarmer.postInit();
		}
		
		for (BlockCapacitor blockCapacitor : capacitor) {
			blockCapacitor.postInit();
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
	
	//crafters
	public static List<BlockCrafter> crafter = new ArrayList<BlockCrafter>(4);
	
	//killers
	public static List<BlockKiller> killer = new ArrayList<BlockKiller>(4);
		
	//farmers
	public static List<BlockFarmer> farmer = new ArrayList<BlockFarmer>(4);
	
	//capacitors
	public static List<BlockCapacitor> capacitor = new ArrayList<BlockCapacitor>(4);

}
