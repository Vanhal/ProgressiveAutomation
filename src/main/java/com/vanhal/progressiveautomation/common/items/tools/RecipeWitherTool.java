package com.vanhal.progressiveautomation.common.items.tools;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeWitherTool extends ShapedOreRecipe {

    protected ItemStack output = ItemStack.EMPTY;

    public RecipeWitherTool(Item result, Object... recipe) {
        super(null, new ItemStack(result), recipe);
        setRegistryName(getRecipeOutput().getItem().getRegistryName());
        output = new ItemStack(result);
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = output.copy();
        result.addEnchantment(Enchantments.UNBREAKING, 10);
        result.setStackDisplayName("Withered " + result.getDisplayName());
        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack result = output.copy();
        result.addEnchantment(Enchantments.UNBREAKING, 10);
        result.setStackDisplayName("Withered " + result.getDisplayName());
        return result;
    }
}