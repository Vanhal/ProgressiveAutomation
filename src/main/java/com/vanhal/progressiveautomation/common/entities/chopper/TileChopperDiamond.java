package com.vanhal.progressiveautomation.common.entities.chopper;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileChopperDiamond extends TileChopper {

    public TileChopperDiamond() {
        super();
        setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER);
        allowSheer();
        setUpgradeLevel(ToolHelper.LEVEL_MAX);
    }
}