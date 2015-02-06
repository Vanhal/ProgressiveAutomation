package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class BaseItem extends Item {
	public String itemName;
	
	public BaseItem() {
		
	}
	
	public BaseItem(String name) {
		setName(name);
		setCreativeTab(ProgressiveAutomation.PATab);
	}
	
	
	public void setName(String newName) {
		itemName = newName;
		this.setUnlocalizedName(itemName);
	}
	
	public void preInit() {
		GameRegistry.registerItem(this, itemName);
		addUpgradeRecipe();
	}

	protected void addUpgradeRecipe() {
		
	}

	protected void addNormalRecipe() {
		
	}
}
