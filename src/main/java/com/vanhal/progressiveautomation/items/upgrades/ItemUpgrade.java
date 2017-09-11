package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.items.BaseItem;
import com.vanhal.progressiveautomation.upgrades.UpgradeRegistry;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public abstract class ItemUpgrade extends BaseItem {
	private UpgradeType type;
	
	public ItemUpgrade(String name, UpgradeType type) {
		super(name);
		this.type = type;
	}
	
	public UpgradeType getType() {
		return type;
	}

	public int allowedAmount() { return 1; }
	
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
