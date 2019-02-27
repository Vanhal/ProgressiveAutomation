package com.vanhal.progressiveautomation.common.entities.miner;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileMinerIron extends TileMiner {

    public TileMinerIron() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_IRON);
        setAllowedUpgrades(UpgradeType.IRON, UpgradeType.WITHER, UpgradeType.COBBLE_GEN, UpgradeType.FILLER);
    }
}