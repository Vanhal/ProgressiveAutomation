package com.vanhal.progressiveautomation.common.entities.planter;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TilePlanterIron extends TilePlanter {

    public TilePlanterIron() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_IRON);
        setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER);
        setHarvestTime(20);
    }
}