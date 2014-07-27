package com.vanhal.progressiveautomation.entities.planter;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TilePlanterDiamond extends TilePlanter {

	public TilePlanterDiamond() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_DIAMOND);
		setHarvestTime(10);
	}
}
