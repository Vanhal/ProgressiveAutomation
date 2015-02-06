package com.vanhal.progressiveautomation.items.upgrades;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemIronUpgrade extends ItemTieredUpgrade {
	public ItemIronUpgrade() {
		super("IronUpgrade", UpgradeType.IRON, ToolHelper.LEVEL_IRON);
		//this.setTextureName(Ref.MODID+":Iron_Upgrade");
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.iron_ingot, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"p p", "prp", "p p", 'p', Items.iron_ingot, 'r', PAItems.stoneUpgrade});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addTieredRecipe(Item previousTier) {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"p p", "prp", "p p", 'p', Items.iron_ingot, 'r', previousTier});
		GameRegistry.addRecipe(recipe);
	}
}
