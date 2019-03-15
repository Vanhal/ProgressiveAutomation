package com.vanhal.progressiveautomation.common.compat.mods;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.common.compat.BaseMod;
import com.vanhal.progressiveautomation.common.util.OreHelper;
import net.minecraft.item.ItemStack;

public class TiCon extends BaseMod {

    public TiCon() {
        modID = "tconstruct";
    }

    @Override
    public boolean shouldLoad() {
        ProgressiveAutomation.logger.info("TiCon Loaded");
        return true;
    }

    @Override
    public boolean isLog(ItemStack stack) {
        if (OreHelper.testOre("blockSlimeCongealed", stack)) {
            return true;
        }
        return false;
    }
}