package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemWoodUpgrade extends ItemUpgrade {
	
	public ItemWoodUpgrade() {
		super("WoodUpgrade", ToolInfo.LEVEL_WOOD);
		this.setTextureName(Ref.MODID+":Wood_Upgrade");
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "prp", "ppp", 'p', Blocks.log, 'r', Items.redstone});
		GameRegistry.addRecipe(recipe);
	}
}
