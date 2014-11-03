package com.vanhal.progressiveautomation.items.tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.vanhal.progressiveautomation.items.BaseItem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWitherDiamond extends BaseItem {
	public ItemWitherDiamond() {
		super("WitherDiamond");
		setTextureName("Minecraft:diamond");
	}
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
	
	protected void addNormalRecipe() {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this, 1), Items.nether_star, Items.diamond);
		GameRegistry.addRecipe(recipe);
	}

	protected void addUpgradeRecipe() {
		addNormalRecipe();
	}
}
