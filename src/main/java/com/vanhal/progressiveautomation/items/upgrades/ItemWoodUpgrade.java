package com.vanhal.progressiveautomation.items.upgrades;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemWoodUpgrade extends ItemTieredUpgrade {
	
	public ItemWoodUpgrade() {
		super("WoodUpgrade", UpgradeType.WOODEN, ToolHelper.LEVEL_WOOD);
		this.setTextureName(Ref.MODID+":Wood_Upgrade");
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', "logWood", 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		addNormalRecipe();
	}
	
	@Override
	protected void addTieredRecipe(Item previousTier) {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', "logWood", 'r', previousTier});
		GameRegistry.addRecipe(recipe);
	}
}
