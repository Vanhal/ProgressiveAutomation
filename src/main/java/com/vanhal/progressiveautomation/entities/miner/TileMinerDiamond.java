package com.vanhal.progressiveautomation.entities.miner;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileMinerDiamond extends TileMiner {

	public TileMinerDiamond() {
		super();
		setUpgradeLevel(10);
		setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER, UpgradeType.COBBLE_GEN, UpgradeType.FILLER);
	}
}
