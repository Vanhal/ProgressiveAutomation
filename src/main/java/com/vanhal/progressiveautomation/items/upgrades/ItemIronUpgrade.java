package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.item.Item;

public class ItemIronUpgrade extends ItemTieredUpgrade {
	public ItemIronUpgrade() {
		super("IronUpgrade", UpgradeType.IRON, ToolHelper.LEVEL_IRON);
		//this.setTextureName(Ref.MODID+":Iron_Upgrade");
	}
	
	@Override
	protected void addTieredRecipe(Item previousTier) {
	}
}
