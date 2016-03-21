package com.vanhal.progressiveautomation.entities.chopper;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileChopperStone extends TileChopper {

	public TileChopperStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER);
		allowSheer();
	}
}
