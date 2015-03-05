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

public class ItemFilterMobUpgrade extends ItemUpgrade {

	public ItemFilterMobUpgrade() {
		super("FilterMobUpgrade", UpgradeType.FILTER_MOB);
		this.setTextureName(Ref.MODID+":Filter_Mob_Upgrade");
	}

	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(EnumChatFormatting.GRAY + "Will make the Killer to kill enemy mobs only");
       
    }
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"pgp", "zrz", "pgp", 'p', Blocks.stone, 'r', Blocks.redstone_block, 'z', Items.rotten_flesh, 'g', Items.gunpowder});
		GameRegistry.addRecipe(recipe);
	}
}
