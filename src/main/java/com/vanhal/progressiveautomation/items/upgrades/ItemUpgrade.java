package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.items.BaseItem;
import com.vanhal.progressiveautomation.upgrades.UpgradeRegistry;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
	public void preInit() {
		super.preInit();
		UpgradeRegistry.registerUpgradeItem(this.getType(), this);
	}
}
