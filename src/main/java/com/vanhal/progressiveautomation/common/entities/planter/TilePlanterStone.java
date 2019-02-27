package com.vanhal.progressiveautomation.common.entities.planter;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TilePlanterStone extends TilePlanter {

    public TilePlanterStone() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_STONE);
        setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER);
        setHarvestTime(40);
    }
}