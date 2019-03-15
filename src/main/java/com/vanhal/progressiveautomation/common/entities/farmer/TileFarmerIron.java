package com.vanhal.progressiveautomation.common.entities.farmer;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileFarmerIron extends TileFarmer {

    public TileFarmerIron() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_IRON);
        setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
        setWaitTime(20);
    }
}