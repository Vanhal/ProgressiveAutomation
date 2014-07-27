package com.vanhal.progressiveautomation.entities.planter;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TilePlanterIron extends TilePlanter {

	public TilePlanterIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setHarvestTime(20);
	}
}
