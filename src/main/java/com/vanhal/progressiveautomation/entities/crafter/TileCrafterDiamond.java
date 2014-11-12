package com.vanhal.progressiveautomation.entities.crafter;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TileCrafterDiamond extends TileCrafter {

	public TileCrafterDiamond() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_DIAMOND);
		setCraftTime(4);
	}
}
