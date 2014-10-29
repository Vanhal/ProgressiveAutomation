package com.vanhal.progressiveautomation.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCobbleGenUpgrade extends ItemUpgrade {
	public ItemCobbleGenUpgrade() {
		super("CobbleUpgrade", -1);
		this.setTextureName(Ref.MODID+":Cobble_Upgrade");
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "lrw", "ppp", 'p', Blocks.stone, 'r', PABlocks.miner.get(0), 'l', Items.lava_bucket, 'w', Items.water_bucket});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		this.addNormalRecipe();
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(EnumChatFormatting.GRAY + "Uses extra pick durability to mine cobble from an internal cobble gen");
       
    }
}
