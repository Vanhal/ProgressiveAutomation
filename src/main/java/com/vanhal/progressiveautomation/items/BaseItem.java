package com.vanhal.progressiveautomation.items;

import net.minecraft.item.Item;

public class BaseItem extends Item {

    public BaseItem() {
    }

    public void preInit() {
        addUpgradeRecipe();
    }

    public void init() {
    }

    protected void addUpgradeRecipe() {
    }

    protected void addNormalRecipe() {
    }
}