package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFilterAdultUpgrade extends ItemUpgrade {

	public ItemFilterAdultUpgrade() {
		super("FilterAdultUpgrade", UpgradeType.FILTER_ADULT);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final World worldIn,
			final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Will make the Killer to kill adults only");
	   
	}
	
	/*
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"pep", "lrw", "pep", 'p', Blocks.STONE, 'r', Blocks.REDSTONE_BLOCK, 'e', Items.EGG, 'l', Items.LEATHER, 'w', Blocks.WOOL});
		GameRegistry.addRecipe(recipe);
	}*/

}
