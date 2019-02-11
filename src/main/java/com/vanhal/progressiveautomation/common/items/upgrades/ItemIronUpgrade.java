package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.common.util.ToolHelper;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import net.minecraft.item.Item;

public class ItemIronUpgrade extends ItemTieredUpgrade {

    public ItemIronUpgrade() {
        super(UpgradeType.IRON, ToolHelper.LEVEL_IRON);
    }

    @Override
    protected void addTieredRecipe(Item previousTier) {
    }
}