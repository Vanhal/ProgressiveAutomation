package com.vanhal.progressiveautomation.common.compat.mods;

import com.vanhal.progressiveautomation.common.compat.BaseMod;
import net.minecraft.item.ItemStack;

public class IntegratedDynamics extends BaseMod {

    public IntegratedDynamics() {
        modID = "integrateddynamics";
    }

    public boolean isLog(ItemStack item) {
        //the item may be hiding in a delegate, so use getItem()
        if (item.isEmpty()) {
            return false;
        }

        if (item.getItem() == null) {
            return false;
        }
        return (item.getItem().getTranslationKey().contains("integrateddynamics.menril_log"));    //NEED TO CONFIRM THIS NAME
    }
}