package com.vanhal.progressiveautomation.entities.farmer;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileFarmerIron extends TileFarmer {

	public TileFarmerIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
		setWaitTime(20);
	}
}
