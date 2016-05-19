package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemIronUpgrade extends ItemTieredUpgrade {
	public ItemIronUpgrade() {
		super("IronUpgrade", UpgradeType.IRON, ToolHelper.LEVEL_IRON);
		//this.setTextureName(Ref.MODID+":Iron_Upgrade");
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.IRON_INGOT, 'r', Items.REDSTONE});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"p p", "prp", "p p", 'p', Items.IRON_INGOT, 'r', PAItems.stoneUpgrade});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addTieredRecipe(Item previousTier) {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"p p", "prp", "p p", 'p', Items.IRON_INGOT, 'r', previousTier});
		GameRegistry.addRecipe(recipe);
	}
}
