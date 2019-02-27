package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.item.Item;

public class ItemStoneUpgrade extends ItemTieredUpgrade {

    public ItemStoneUpgrade() {
        super(UpgradeType.STONE, ToolHelper.LEVEL_STONE);
    }

    @Override
    protected void addTieredRecipe(Item previousTier) {
    }
}