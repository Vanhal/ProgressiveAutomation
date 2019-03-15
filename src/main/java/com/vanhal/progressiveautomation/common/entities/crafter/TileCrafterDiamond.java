package com.vanhal.progressiveautomation.common.entities.crafter;

import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileCrafterDiamond extends TileCrafter {

    public TileCrafterDiamond() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_DIAMOND);
        setCraftTime(4);
    }
}