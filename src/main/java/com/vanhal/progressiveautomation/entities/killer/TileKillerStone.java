package com.vanhal.progressiveautomation.entities.killer;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public class TileKillerStone extends TileKiller {

	public TileKillerStone() {
		super();
		setUpgradeLevel(ToolHelper.LEVEL_STONE);
		setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER, UpgradeType.FILTER_ADULT, UpgradeType.FILTER_ANIMAL, UpgradeType.FILTER_MOB, UpgradeType.FILTER_PLAYER);
		setAttackTime(20);
	}
}
