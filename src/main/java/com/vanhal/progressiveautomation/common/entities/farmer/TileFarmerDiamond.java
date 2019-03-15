package com.vanhal.progressiveautomation.common.entities.farmer;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileFarmerDiamond extends TileFarmer {

    public TileFarmerDiamond() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_DIAMOND);
        setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
        setWaitTime(ToolHelper.LEVEL_MAX);
    }
}