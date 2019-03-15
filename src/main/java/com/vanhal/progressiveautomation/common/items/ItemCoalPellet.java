package com.vanhal.progressiveautomation.common.items;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemCoalPellet extends Item {

    protected int burnTime = 100;

    public ItemCoalPellet() {
        int coalTime = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL));
        burnTime = coalTime / 8;
    }

    public int getBurnTime(ItemStack fuel) {
        if (fuel.isItemEqual(new ItemStack(this))) {
            return burnTime;
        } else {
            return 0;
        }
    }
}