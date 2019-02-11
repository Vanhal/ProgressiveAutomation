package com.vanhal.progressiveautomation.common.entities.chopper;

import com.vanhal.progressiveautomation.common.util.ToolHelper;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;

public class TileChopperStone extends TileChopper {

    public TileChopperStone() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_STONE);
        setAllowedUpgrades(UpgradeType.STONE, UpgradeType.WITHER);
        allowSheer();
    }
}
