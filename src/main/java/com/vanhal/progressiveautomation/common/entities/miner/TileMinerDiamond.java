package com.vanhal.progressiveautomation.common.entities.miner;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileMinerDiamond extends TileMiner {

    public TileMinerDiamond() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_MAX);
        setAllowedUpgrades(UpgradeType.DIAMOND, UpgradeType.WITHER, UpgradeType.COBBLE_GEN, UpgradeType.FILLER);
    }
}