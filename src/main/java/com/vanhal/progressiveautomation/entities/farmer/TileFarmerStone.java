package com.vanhal.progressiveautomation.entities.farmer;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileFarmerStone extends TileFarmer {

	public TileFarmerStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
		setWaitTime(40);
	}
}
