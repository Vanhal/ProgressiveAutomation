package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;

import cpw.mods.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
	}
	
}
