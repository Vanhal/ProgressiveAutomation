package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDiamondUpgrade extends ItemUpgrade {
	public ItemDiamondUpgrade() {
		super("DiamondUpgrade", ToolInfo.LEVEL_DIAMOND);
		this.setTextureName(Ref.MODID+":Diamond_Upgrade");
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.diamond, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.diamond, 'r', PAItems.ironUpgrade});
		GameRegistry.addRecipe(recipe);
	}
}
