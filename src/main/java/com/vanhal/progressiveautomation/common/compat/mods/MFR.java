package com.vanhal.progressiveautomation.common.compat.mods;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.common.compat.BaseMod;
import net.minecraft.item.ItemStack;

public class MFR extends BaseMod {

    public MFR() {
        modID = "mfr";
    }

    @Override
    public boolean shouldLoad() {
        ProgressiveAutomation.logger.info("MFR Loaded");
        return true;
    }

    @Override
    public boolean isSapling(ItemStack stack) {
        if ((!stack.isEmpty()) && (stack.getItem() != null)) {
            return (stack.getTranslationKey().compareToIgnoreCase("tile.mfr.rubberwood.sapling") == 0);
        }
        return false;
    }
}