package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.item.Item;

public class ItemStoneUpgrade extends ItemTieredUpgrade {
	public ItemStoneUpgrade() {
		super("StoneUpgrade", UpgradeType.STONE, ToolHelper.LEVEL_STONE);
	}
	
/*	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.STONE, 'r', Items.REDSTONE});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.STONE, 'r', PAItems.woodUpgrade});
		GameRegistry.addRecipe(recipe);
	}
*/	
	@Override
	protected void addTieredRecipe(Item previousTier) {
/*		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.STONE, 'r', previousTier});
		GameRegistry.addRecipe(recipe);
*/	}
}
