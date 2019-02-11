package com.vanhal.progressiveautomation.common.entities.killer;

import com.vanhal.progressiveautomation.common.util.ToolHelper;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;

public class TileKillerIron extends TileKiller {

    public TileKillerIron() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_IRON);
        setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER, UpgradeType.FILTER_ADULT, UpgradeType.FILTER_ANIMAL, UpgradeType.FILTER_MOB, UpgradeType.FILTER_PLAYER);
        setAttackTime(10);
    }
}