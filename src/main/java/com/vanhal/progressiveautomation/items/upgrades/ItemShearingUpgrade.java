package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShearingUpgrade extends ItemUpgrade {
	
	public ItemShearingUpgrade() {
		super("ShearingUpgrade", UpgradeType.SHEARING);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, final World worldIn,
			final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Allows the Farmer shear animals");
	   
	}
	
/*	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"plp", "wsw", "plp", 'p', Blocks.STONE, 's', Items.SHEARS, 'l', Items.LEATHER, 'w', Blocks.WOOL});
		GameRegistry.addRecipe(recipe);
	}
*/}
