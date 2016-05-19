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

public class ItemFilterMobUpgrade extends ItemUpgrade {

	public ItemFilterMobUpgrade() {
		super("FilterMobUpgrade", UpgradeType.FILTER_MOB);
	}

	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(TextFormatting.GRAY + "Will make the Killer to kill enemy mobs only");
       
    }
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"pgp", "zrz", "pgp", 'p', Blocks.STONE, 'r', Blocks.REDSTONE_BLOCK, 'z', Items.ROTTEN_FLESH, 'g', Items.GUNPOWDER});
		GameRegistry.addRecipe(recipe);
	}
}
