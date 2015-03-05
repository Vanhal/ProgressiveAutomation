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

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMilkerUpgrade extends ItemUpgrade {

	public ItemMilkerUpgrade() {
		super("MilkerUpgrade", UpgradeType.MILKER);
		this.setTextureName(Ref.MODID+":Milker_Upgrade");
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(EnumChatFormatting.GRAY + "Allows the Farmer to Milk animals into buckets");
	   
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "brb", "ppp", 'p', Blocks.stone, 'r', Blocks.redstone_block, 'b', Items.milk_bucket});
		GameRegistry.addRecipe(recipe);
	}

}
