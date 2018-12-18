package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFilterMobUpgrade extends ItemUpgrade {

	public ItemFilterMobUpgrade() {
		super("FilterMobUpgrade", UpgradeType.FILTER_MOB);
	}

	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(final ItemStack stack, final World worldIn,
			final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Will make the Killer to kill enemy mobs only");
       
    }
	
/*	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"pgp", "zrz", "pgp", 'p', Blocks.STONE, 'r', Blocks.REDSTONE_BLOCK, 'z', Items.ROTTEN_FLESH, 'g', Items.GUNPOWDER});
		GameRegistry.addRecipe(recipe);
	}
*/}
