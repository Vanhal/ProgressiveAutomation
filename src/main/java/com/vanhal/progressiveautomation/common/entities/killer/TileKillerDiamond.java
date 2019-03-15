package com.vanhal.progressiveautomation.common.entities.killer;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileKillerDiamond extends TileKiller {

    public TileKillerDiamond() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_MAX);
        setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER, UpgradeType.FILTER_ADULT, UpgradeType.FILTER_ANIMAL, UpgradeType.FILTER_MOB, UpgradeType.FILTER_PLAYER);
        setAttackTime(5);
    }
}