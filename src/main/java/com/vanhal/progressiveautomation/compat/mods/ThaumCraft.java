package com.vanhal.progressiveautomation.compat.mods;

import com.vanhal.progressiveautomation.compat.BaseMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ThaumCraft extends BaseMod {

    public ThaumCraft() {
        modID = "thaumcraft";
    }

    @Override
    public boolean isLog(ItemStack item) {
        if (item.isEmpty()) {
            return false;
        }

        // the item may be hiding in a delegate, so use getItem()
        if (item.getItem() == null) {
            return false;
        }
        return OreDictionary.containsMatch(true, OreDictionary.getOres("logWood"), item);
    }

    @Override
    public boolean isLeaf(ItemStack item) {
        if (item.isEmpty()) {
            return false;
        }

        // the item may be hiding in a delegate, so use getItem()
        if (item.getItem() == null) {
            return false;
        }
        return OreDictionary.containsMatch(true, OreDictionary.getOres("treeLeaves"), item);
    }
}