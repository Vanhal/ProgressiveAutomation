package com.vanhal.progressiveautomation.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemManual extends BaseItem {
	public ItemManual() {
		super("Manual");
		//setTextureName(Ref.MODID+":Manual");
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
}
