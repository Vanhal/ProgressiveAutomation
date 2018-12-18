package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.item.Item;

public abstract class ItemTieredUpgrade extends ItemUpgrade {
	
	private final int level;
	
	public ItemTieredUpgrade(String name, UpgradeType type, int level) {
		super(name, type);
		this.level = level;
	}
	
	public void preInit(Item previousTier) {
/*		GameRegistry.register(this);
		UpgradeRegistry.registerUpgradeItem(this.getType(), this);
		addTieredRecipe(previousTier);
*/	}
	
	protected abstract void addTieredRecipe(Item previousTier);
	
	public int getLevel() {
		return level;
	}

	@Override
	public int allowedAmount() {
		return PAConfig.maxRangeUpgrades;
	}
}
