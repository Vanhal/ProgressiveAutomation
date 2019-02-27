package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.item.Item;

public class ItemWoodUpgrade extends ItemTieredUpgrade {

    public ItemWoodUpgrade() {
        super(UpgradeType.WOODEN, ToolHelper.LEVEL_WOOD);
    }

    @Override
    protected void addTieredRecipe(Item previousTier) {
    }
}