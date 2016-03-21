package com.vanhal.progressiveautomation.entities.chopper;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileChopperIron extends TileChopper {

	public TileChopperIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER);
		allowSheer();
	}
}