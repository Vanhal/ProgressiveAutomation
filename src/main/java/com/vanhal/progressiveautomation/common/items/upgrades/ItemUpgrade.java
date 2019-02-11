package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.common.items.BaseItem;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeRegistry;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;

public abstract class ItemUpgrade extends BaseItem {

    private UpgradeType type;

    public ItemUpgrade(UpgradeType type) {
        this.type = type;
    }

    public UpgradeType getType() {
        return type;
    }

    public int allowedAmount() {
        return 1;
    }

    @Override
    protected void addUpgradeRecipe() {
        this.addNormalRecipe();
    }

    @Override
    public void preInit() {
        super.preInit();
        UpgradeRegistry.registerUpgradeItem(this.getType(), this);
    }
}
