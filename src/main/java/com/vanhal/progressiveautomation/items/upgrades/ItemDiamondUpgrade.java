package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.item.Item;

public class ItemDiamondUpgrade extends ItemTieredUpgrade {

    public ItemDiamondUpgrade() {
        super(UpgradeType.DIAMOND, ToolHelper.LEVEL_DIAMOND);
    }

    @Override
    protected void addTieredRecipe(Item previousTier) {
    }
}