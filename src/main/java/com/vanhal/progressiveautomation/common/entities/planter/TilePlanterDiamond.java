package com.vanhal.progressiveautomation.common.entities.planter;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TilePlanterDiamond extends TilePlanter {

    public TilePlanterDiamond() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_MAX);
        setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER);
        setHarvestTime(10);
    }
}