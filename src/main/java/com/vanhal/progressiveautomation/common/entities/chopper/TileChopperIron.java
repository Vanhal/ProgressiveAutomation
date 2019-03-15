package com.vanhal.progressiveautomation.common.entities.chopper;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileChopperIron extends TileChopper {

    public TileChopperIron() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_IRON);
        setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER);
        allowSheer();
    }
}