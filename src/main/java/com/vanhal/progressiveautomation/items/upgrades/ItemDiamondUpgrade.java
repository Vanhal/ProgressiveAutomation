package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemDiamondUpgrade extends ItemTieredUpgrade {
	public ItemDiamondUpgrade() {
		super("DiamondUpgrade", UpgradeType.DIAMOND, ToolHelper.LEVEL_DIAMOND);
		//this.setTextureName(Ref.MODID+":Diamond_Upgrade");
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Items.diamond, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this), PAItems.ironUpgrade, Items.diamond, Items.diamond);
		//ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
		//	"ppp", "prp", "ppp", 'p', Items.diamond, 'r', PAItems.ironUpgrade});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addTieredRecipe(Item previousTier) {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this), previousTier, Items.diamond, Items.diamond);
		//ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
		//	"ppp", "prp", "ppp", 'p', Items.diamond, 'r', PAItems.ironUpgrade});
		GameRegistry.addRecipe(recipe);
	}
}
