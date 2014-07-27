package com.vanhal.progressiveautomation.entities.planter;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TilePlanterStone extends TilePlanter {

	public TilePlanterStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setHarvestTime(40);
	}
}
