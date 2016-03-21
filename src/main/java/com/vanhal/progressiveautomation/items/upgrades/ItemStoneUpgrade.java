package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemStoneUpgrade extends ItemTieredUpgrade {
	public ItemStoneUpgrade() {
		super("StoneUpgrade", UpgradeType.STONE, ToolHelper.LEVEL_STONE);
		//this.setTextureName(Ref.MODID+":Stone_Upgrade");
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.stone, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.stone, 'r', PAItems.woodUpgrade});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addTieredRecipe(Item previousTier) {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.stone, 'r', previousTier});
		GameRegistry.addRecipe(recipe);
	}
}
