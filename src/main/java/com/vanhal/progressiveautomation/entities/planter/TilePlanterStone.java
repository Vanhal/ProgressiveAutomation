package com.vanhal.progressiveautomation.entities.planter;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TilePlanterStone extends TilePlanter {

	public TilePlanterStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER);
		setHarvestTime(40);
	}
}
