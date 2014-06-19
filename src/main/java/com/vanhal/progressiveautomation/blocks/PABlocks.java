package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolHelper;

public class PABlocks {

	public static void preInit() {
		//miners
		if (PAConfig.minerEnabled && PAConfig.allowWoodenLevel) woodenMiner = new BlockMiner(ToolHelper.LEVEL_WOOD);
		if (PAConfig.minerEnabled && PAConfig.allowStoneLevel) stoneMiner = new BlockMiner(ToolHelper.LEVEL_STONE);
		if (PAConfig.minerEnabled && PAConfig.allowIronLevel) ironMiner = new BlockMiner(ToolHelper.LEVEL_IRON);
		if (PAConfig.minerEnabled && PAConfig.allowDiamondLevel) diamondMiner = new BlockMiner(ToolHelper.LEVEL_DIAMOND);
		
		//choppers
		if (PAConfig.chopperEnabled && PAConfig.allowWoodenLevel) woodenChopper = new BlockChopper(ToolHelper.LEVEL_WOOD);
		if (PAConfig.chopperEnabled && PAConfig.allowStoneLevel) stoneChopper = new BlockChopper(ToolHelper.LEVEL_STONE);
		if (PAConfig.chopperEnabled && PAConfig.allowIronLevel) ironChopper = new BlockChopper(ToolHelper.LEVEL_IRON);
		if (PAConfig.chopperEnabled && PAConfig.allowDiamondLevel) diamondChopper = new BlockChopper(ToolHelper.LEVEL_DIAMOND);
		
		//generators
		if (PAConfig.generatorEnabled && PAConfig.rfSupport && PAConfig.allowWoodenLevel)
			woodenGenerator = new BlockGenerator(ToolHelper.LEVEL_WOOD);
		if (PAConfig.generatorEnabled && PAConfig.rfSupport && PAConfig.allowStoneLevel)
			stoneGenerator = new BlockGenerator(ToolHelper.LEVEL_STONE);
		if (PAConfig.generatorEnabled && PAConfig.rfSupport && PAConfig.allowIronLevel)
			ironGenerator = new BlockGenerator(ToolHelper.LEVEL_IRON);
		if (PAConfig.generatorEnabled && PAConfig.rfSupport && PAConfig.allowDiamondLevel)
			diamondGenerator = new BlockGenerator(ToolHelper.LEVEL_DIAMOND);
		
		//preInit
		if (woodenMiner!=null) woodenMiner.preInit();
		if (stoneMiner!=null) stoneMiner.preInit();
		if (ironMiner!=null) ironMiner.preInit();
		if (diamondMiner!=null) diamondMiner.preInit();
		
		if (woodenChopper!=null) woodenChopper.preInit();
		if (stoneChopper!=null) stoneChopper.preInit();
		if (ironChopper!=null) ironChopper.preInit();
		if (diamondChopper!=null) diamondChopper.preInit();
		
		if (woodenGenerator!=null) woodenGenerator.preInit();
		if (stoneGenerator!=null) stoneGenerator.preInit();
		if (ironGenerator!=null) ironGenerator.preInit();
		if (diamondGenerator!=null) diamondGenerator.preInit();
	}
	
	public static void init() {
		
		if (woodenMiner!=null) woodenMiner.init();
		if (stoneMiner!=null) stoneMiner.init();
		if (ironMiner!=null) ironMiner.init();
		if (diamondMiner!=null) diamondMiner.init();
		
		if (woodenChopper!=null) woodenChopper.init();
		if (stoneChopper!=null) stoneChopper.init();
		if (ironChopper!=null) ironChopper.init();
		if (diamondChopper!=null) diamondChopper.init();
		
		if (woodenGenerator!=null) woodenGenerator.init();
		if (stoneGenerator!=null) stoneGenerator.init();
		if (ironGenerator!=null) ironGenerator.init();
		if (diamondGenerator!=null) diamondGenerator.init();
	}
	
	public static void postInit() {
		
		if (woodenMiner!=null) woodenMiner.postInit();
		if (stoneMiner!=null) stoneMiner.postInit();
		if (ironMiner!=null) ironMiner.postInit();
		if (diamondMiner!=null) diamondMiner.postInit();
		
		if (woodenChopper!=null) woodenChopper.postInit();
		if (stoneChopper!=null) stoneChopper.postInit();
		if (ironChopper!=null) ironChopper.postInit();
		if (diamondChopper!=null) diamondChopper.postInit();
		
		if (woodenGenerator!=null) woodenGenerator.postInit();
		if (stoneGenerator!=null) stoneGenerator.postInit();
		if (ironGenerator!=null) ironGenerator.postInit();
		if (diamondGenerator!=null) diamondGenerator.postInit();
	}
	
	//blocks
	
	//miners
	public static BlockMiner woodenMiner = null;
	public static BlockMiner stoneMiner = null;
	public static BlockMiner ironMiner = null;
	public static BlockMiner diamondMiner = null;
	
	//choppers
	public static BlockChopper woodenChopper = null;
	public static BlockChopper stoneChopper = null;
	public static BlockChopper ironChopper = null;
	public static BlockChopper diamondChopper = null;
	
	//generators
	public static BlockGenerator woodenGenerator = null;
	public static BlockGenerator stoneGenerator = null;
	public static BlockGenerator ironGenerator = null;
	public static BlockGenerator diamondGenerator = null;
}
