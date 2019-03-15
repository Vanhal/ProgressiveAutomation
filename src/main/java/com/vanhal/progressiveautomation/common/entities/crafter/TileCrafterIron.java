package com.vanhal.progressiveautomation.common.entities.crafter;

import com.vanhal.progressiveautomation.common.util.ToolHelper;

public class TileCrafterIron extends TileCrafter {

    public TileCrafterIron() {
        super();
        setUpgradeLevel(ToolHelper.LEVEL_IRON);
        setCraftTime(20);
    }
}