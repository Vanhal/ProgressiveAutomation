package com.vanhal.progressiveautomation.common.entities.farmer;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileFarmerStone extends TileFarmer {

    public TileFarmerStone() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_STONE);
        setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
        setWaitTime(40);
    }
}