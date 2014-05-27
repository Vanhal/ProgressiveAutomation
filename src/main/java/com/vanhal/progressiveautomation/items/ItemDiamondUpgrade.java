package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemDiamondUpgrade extends ItemUpgrade {
	public ItemDiamondUpgrade() {
		super("DiamondUpgrade", ToolHelper.LEVEL_DIAMOND);
		this.setTextureName(Ref.MODID+":Diamond_Upgrade");
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.diamond, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this), PAItems.ironUpgrade, Items.diamond, Items.diamond);
		//ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
		//	"ppp", "prp", "ppp", 'p', Items.diamond, 'r', PAItems.ironUpgrade});
		GameRegistry.addRecipe(recipe);
	}
}
