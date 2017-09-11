package com.vanhal.progressiveautomation.entities.crafter;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TileCrafterIron extends TileCrafter {

	public TileCrafterIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setCraftTime(20);
	}
}
