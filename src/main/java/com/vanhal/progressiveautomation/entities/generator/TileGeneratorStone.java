package com.vanhal.progressiveautomation.entities.generator;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TileGeneratorStone extends TileGenerator {

	public TileGeneratorStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setEnergyStorage(40000, 1);
		setFireChance(0.001f);
	}
}
