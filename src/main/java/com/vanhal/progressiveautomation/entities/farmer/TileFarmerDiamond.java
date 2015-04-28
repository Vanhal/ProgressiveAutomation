package com.vanhal.progressiveautomation.entities.farmer;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileFarmerDiamond extends TileFarmer {

	public TileFarmerDiamond() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_DIAMOND);
		setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
		setWaitTime(ToolHelper.LEVEL_MAX);
	}
}
