package com.vanhal.progressiveautomation.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.vanhal.progressiveautomation.ref.Ref;

public class ItemManual extends BaseItem {
	public ItemManual() {
		super("Manual");
		//setTextureName(Ref.MODID+":Manual");
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		return itemStack;
	}
	
	
}
