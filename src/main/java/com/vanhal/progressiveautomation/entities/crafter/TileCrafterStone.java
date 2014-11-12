package com.vanhal.progressiveautomation.entities.crafter;

import com.vanhal.progressiveautomation.ref.ToolHelper;

public class TileCrafterStone extends TileCrafter {
	
	public TileCrafterStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setCraftTime(60);
	}
}
