package com.vanhal.progressiveautomation.entities.killer;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileKillerIron extends TileKiller {

	public TileKillerIron() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_IRON);
		setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER, UpgradeType.FILTER_ADULT, UpgradeType.FILTER_ANIMAL, UpgradeType.FILTER_MOB, UpgradeType.FILTER_PLAYER);
		setAttackTime(10);
	}
}
