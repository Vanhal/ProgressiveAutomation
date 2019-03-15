package com.vanhal.progressiveautomation.common.items.tools;

import com.google.gson.JsonObject;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class WitherRecipe extends ShapedRecipes {

	public WitherRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(group, width, height, ingredients, result);
	}
	
	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		ItemStack output = super.getCraftingResult(inv); // Get the default output
		if (!output.isEmpty()) {
			output.addEnchantment(Enchantments.UNBREAKING, 10);
			output.setStackDisplayName("Withered "+output.getDisplayName());
		}
		return output;
	}

	@Override
    public ItemStack getRecipeOutput() {
		ItemStack result = super.getRecipeOutput().copy();
    	result.addEnchantment(Enchantments.UNBREAKING, 10);
    	result.setStackDisplayName("Withered "+result.getDisplayName());
    	return result; 
    }
	
	public static class Factory implements IRecipeFactory {

		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			ShapedRecipes input = ShapedRecipes.deserialize(json);

			return new WitherRecipe(input.getGroup(), input.getWidth(), input.getHeight(), input.getIngredients(), input.getRecipeOutput());
		}
	}

}
