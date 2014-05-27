package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemIronUpgrade extends ItemUpgrade {
	public ItemIronUpgrade() {
		super("IronUpgrade", ToolHelper.LEVEL_IRON);
		this.setTextureName(Ref.MODID+":Iron_Upgrade");
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.iron_ingot, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"p p", "prp", "p p", 'p', Items.iron_ingot, 'r', PAItems.stoneUpgrade});
		GameRegistry.addRecipe(recipe);
	}
}
