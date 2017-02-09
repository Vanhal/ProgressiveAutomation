package com.vanhal.progressiveautomation.items.tools;

import java.util.List;

import com.vanhal.progressiveautomation.items.BaseItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemWitherGold extends BaseItem {
	public ItemWitherGold() {
		super("WitherGold");
	}
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    protected void addNormalRecipe() {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this, 2), 
				Items.NETHER_STAR, 
				Items.GOLD_INGOT, 
				Items.GOLD_INGOT
			);
		GameRegistry.addRecipe(recipe);
	}

	protected void addUpgradeRecipe() {
		addNormalRecipe();
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
    	list.add(TextFormatting.GRAY + "Can be used to craft tools");
    }
}
