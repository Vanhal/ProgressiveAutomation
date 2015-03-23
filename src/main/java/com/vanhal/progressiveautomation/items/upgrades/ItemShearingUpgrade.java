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

public class ItemShearingUpgrade extends ItemUpgrade {
	
	public ItemShearingUpgrade() {
		super("ShearingUpgrade", UpgradeType.SHEARING);
		//this.setTextureName(Ref.MODID+":Shearing_Upgrade");
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(EnumChatFormatting.GRAY + "Allows the Farmer shear animals");
	   
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"plp", "wsw", "plp", 'p', Blocks.stone, 's', Items.shears, 'l', Items.leather, 'w', Blocks.wool});
		GameRegistry.addRecipe(recipe);
	}
}
