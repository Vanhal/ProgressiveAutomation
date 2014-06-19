package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperDiamond;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperIron;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperStone;
import com.vanhal.progressiveautomation.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.entities.generator.TileGeneratorDiamond;
import com.vanhal.progressiveautomation.entities.generator.TileGeneratorIron;
import com.vanhal.progressiveautomation.entities.generator.TileGeneratorStone;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.entities.miner.TileMinerStone;

import cpw.mods.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		//miner
		GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
		GameRegistry.registerTileEntity(TileMinerStone.class, "TileMinerStone");
		GameRegistry.registerTileEntity(TileMinerIron.class, "TileMinerIron");
		GameRegistry.registerTileEntity(TileMinerDiamond.class, "TileMinerDiamond");
		
		//chopper
		GameRegistry.registerTileEntity(TileChopper.class, "TileChopper");
		GameRegistry.registerTileEntity(TileChopperStone.class, "TileChopperStone");
		GameRegistry.registerTileEntity(TileChopperIron.class, "TileChopperIron");
		GameRegistry.registerTileEntity(TileChopperDiamond.class, "TileChopperDiamond");
		
		//generator
		GameRegistry.registerTileEntity(TileGenerator.class, "TileGenerator");
		GameRegistry.registerTileEntity(TileGeneratorStone.class, "TileGeneratorStone");
		GameRegistry.registerTileEntity(TileGeneratorIron.class, "TileGeneratorIron");
		GameRegistry.registerTileEntity(TileGeneratorDiamond.class, "TileGeneratorDiamond");
	}
	
}
