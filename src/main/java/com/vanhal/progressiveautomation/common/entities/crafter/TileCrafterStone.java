package com.vanhal.progressiveautomation.common.entities.crafter;

import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileCrafterStone extends TileCrafter {

    public TileCrafterStone() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_STONE);
        setCraftTime(60);
    }
}