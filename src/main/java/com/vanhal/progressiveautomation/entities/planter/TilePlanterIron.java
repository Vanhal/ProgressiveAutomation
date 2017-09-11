package com.vanhal.progressiveautomation.entities.planter;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TilePlanterIron extends TilePlanter {

	public TilePlanterIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER);
		setHarvestTime(20);
	}
}
