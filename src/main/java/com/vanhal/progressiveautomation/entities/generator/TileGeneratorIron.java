package com.vanhal.progressiveautomation.entities.generator;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TileGeneratorIron extends TileGenerator {
	
	public TileGeneratorIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setEnergyStorage(80000, 2);
		setFireChance(0);
	}
}
