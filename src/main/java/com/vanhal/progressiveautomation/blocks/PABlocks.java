package com.vanhal.progressiveautomation.blocks;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PABlocks {
	public static final List<Block> blocks = new ArrayList<>();
	
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

		blocks.addAll(miner);
		blocks.addAll(chopper);
		blocks.addAll(planter);
		blocks.addAll(crafter);
		blocks.addAll(killer);
		blocks.addAll(farmer);
		blocks.addAll(generator);
		blocks.addAll(capacitor);	
	}
	
	@SubscribeEvent
	public void blockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(blocks.toArray(new Block[blocks.size()]));
	}
	
	//miners
	private static List<BlockMiner> miner = new ArrayList<BlockMiner>(4);

	//choppers
	private static List<BlockChopper> chopper = new ArrayList<BlockChopper>(4);

	//planters
	private static List<BlockPlanter> planter = new ArrayList<BlockPlanter>(4);

	//generators
	private static List<BlockGenerator> generator = new ArrayList<BlockGenerator>(4);
	
	//crafters
	private static List<BlockCrafter> crafter = new ArrayList<BlockCrafter>(4);
	
	//killers
	private static List<BlockKiller> killer = new ArrayList<BlockKiller>(4);
		
	//farmers
	private static List<BlockFarmer> farmer = new ArrayList<BlockFarmer>(4);
	
	//capacitors
	private static List<BlockCapacitor> capacitor = new ArrayList<BlockCapacitor>(4);

}
