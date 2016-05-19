package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemFilterAdultUpgrade extends ItemUpgrade {

	public ItemFilterAdultUpgrade() {
		super("FilterAdultUpgrade", UpgradeType.FILTER_ADULT);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(TextFormatting.GRAY + "Will make the Killer to kill adults only");
	   
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"pep", "lrw", "pep", 'p', Blocks.STONE, 'r', Blocks.REDSTONE_BLOCK, 'e', Items.EGG, 'l', Items.LEATHER, 'w', Blocks.WOOL});
		GameRegistry.addRecipe(recipe);
	}

}
