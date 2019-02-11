package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeRegistry;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import net.minecraft.item.Item;

public abstract class ItemTieredUpgrade extends ItemUpgrade {

    private final int level;

    public ItemTieredUpgrade(UpgradeType type, int level) {
        super(type);
        this.level = level;
    }

    public void preInit(Item previousTier) {
        UpgradeRegistry.registerUpgradeItem(this.getType(), this);
    }

    protected abstract void addTieredRecipe(Item previousTier);

    public int getLevel() {
        return level;
    }

    @Override
    public int allowedAmount() {
        return PAConfig.maxRangeUpgrades;
    }
}