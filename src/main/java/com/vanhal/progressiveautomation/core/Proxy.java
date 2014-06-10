package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileChopper;
import com.vanhal.progressiveautomation.entities.TileChopperDiamond;
import com.vanhal.progressiveautomation.entities.TileChopperIron;
import com.vanhal.progressiveautomation.entities.TileChopperStone;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.entities.TileMinerDiamond;
import com.vanhal.progressiveautomation.entities.TileMinerIron;
import com.vanhal.progressiveautomation.entities.TileMinerStone;

import cpw.mods.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
		GameRegistry.registerTileEntity(TileMinerStone.class, "TileMinerStone");
		GameRegistry.registerTileEntity(TileMinerIron.class, "TileMinerIron");
		GameRegistry.registerTileEntity(TileMinerDiamond.class, "TileMinerDiamond");
		GameRegistry.registerTileEntity(TileChopper.class, "TileChopper");
		GameRegistry.registerTileEntity(TileChopperStone.class, "TileChopperStone");
		GameRegistry.registerTileEntity(TileChopperIron.class, "TileChopperIron");
		GameRegistry.registerTileEntity(TileChopperDiamond.class, "TileChopperDiamond");
	}
	
}
