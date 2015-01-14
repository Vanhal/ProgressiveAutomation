package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.items.BaseItem;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemUpgrade extends BaseItem {
	public int level;
	
	public ItemUpgrade() {
		
	}
	
	public ItemUpgrade(String name, int setLevel) {
		super(name);
		setLevel(setLevel);
	}
	
	public void setLevel(int useLevel) {
		level = useLevel;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void preInit(Item previousTier) {
		GameRegistry.registerItem(this, itemName);
		addTieredRecipe(previousTier);
	}
	
	protected void addTieredRecipe(Item previousTier) {
	}
	
}
