package com.vanhal.progressiveautomation.entities.planter;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TilePlanterDiamond extends TilePlanter {

	public TilePlanterDiamond() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_MAX);
		setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER);
		setHarvestTime(10);
	}
}
