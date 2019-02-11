package com.vanhal.progressiveautomation.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemManual extends BaseItem {

    //TODO Finish this up. This was not previously implemented in the game.
    public ItemManual() {
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
    }
}
