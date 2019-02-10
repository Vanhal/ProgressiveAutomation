package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.item.Item;

public class ItemWoodUpgrade extends ItemTieredUpgrade {

    public ItemWoodUpgrade() {
        super(UpgradeType.WOODEN, ToolHelper.LEVEL_WOOD);
    }

    @Override
    protected void addTieredRecipe(Item previousTier) {
    }
}