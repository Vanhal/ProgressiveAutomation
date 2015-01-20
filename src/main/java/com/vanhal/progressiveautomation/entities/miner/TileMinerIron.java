package com.vanhal.progressiveautomation.entities.miner;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileMinerIron extends TileMiner {
	public TileMinerIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER, UpgradeType.COBBLE_GEN, UpgradeType.FILLER);
	}
}