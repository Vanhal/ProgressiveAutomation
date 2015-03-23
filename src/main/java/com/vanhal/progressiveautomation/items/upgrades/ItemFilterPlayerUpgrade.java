package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFilterPlayerUpgrade extends ItemUpgrade {

	public ItemFilterPlayerUpgrade() {
		super("FilterPlayerUpgrade", UpgradeType.FILTER_PLAYER);
		//this.setTextureName(Ref.MODID+":Filter_Player_Upgrade");
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(EnumChatFormatting.GRAY + "Will make the Killer to kill players only");
	   
	}

	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"psp", "srs", "psp", 'p', Blocks.stone, 'r', Blocks.redstone_block, 's', Items.iron_sword});
		GameRegistry.addRecipe(recipe);
	}
}
