package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMilkerUpgrade extends ItemUpgrade {

	public ItemMilkerUpgrade() {
		super("MilkerUpgrade", UpgradeType.MILKER);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final World worldIn,
			final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Allows the Farmer to Milk animals into buckets");
	   
	}
	
/*	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "brb", "ppp", 'p', Blocks.STONE, 'r', Blocks.REDSTONE_BLOCK, 'b', Items.MILK_BUCKET});
		GameRegistry.addRecipe(recipe);
	}
*/
}
