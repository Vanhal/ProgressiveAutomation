package com.vanhal.progressiveautomation.entities.miner;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileMinerStone extends TileMiner {

	public TileMinerStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER, UpgradeType.COBBLE_GEN, UpgradeType.FILLER);
	}
}