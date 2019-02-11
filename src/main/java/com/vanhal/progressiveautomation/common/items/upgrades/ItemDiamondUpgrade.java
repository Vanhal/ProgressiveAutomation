package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.common.util.ToolHelper;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import net.minecraft.item.Item;

public class ItemDiamondUpgrade extends ItemTieredUpgrade {

    public ItemDiamondUpgrade() {
        super(UpgradeType.DIAMOND, ToolHelper.LEVEL_DIAMOND);
    }

    @Override
    protected void addTieredRecipe(Item previousTier) {
    }
}