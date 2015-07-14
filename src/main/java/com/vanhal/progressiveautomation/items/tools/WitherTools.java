package com.vanhal.progressiveautomation.items.tools;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class WitherTools {

	public static void preInit() {
		witherWood = new ItemWitherWood();
		witherStone = new ItemWitherStone();
		witherIron = new ItemWitherIron();
		witherDiamond = new ItemWitherDiamond();
		
		witherWood.preInit();
		witherStone.preInit();
		witherIron.preInit();
		witherDiamond.preInit();
		
		//add recipes for the tools
		woodenRecipes();
		stoneRecipes();
		ironRecipes();
		diamondRecipes();
	}
	
	public static void init() {
		
	}

	public static void postInit() {

	}
	
	protected static void woodenRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.wooden_axe, new Object[]{
				"mm ", "ms ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.wooden_hoe, new Object[]{
				"mm ", " s ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.wooden_pickaxe, new Object[]{
				"mmm", " s ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.wooden_shovel, new Object[]{
				" m ", " s ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.wooden_sword, new Object[]{
				" m ", " m ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void stoneRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.stone_axe, new Object[]{
				"mm ", "ms ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.stone_hoe, new Object[]{
				"mm ", " s ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.stone_pickaxe, new Object[]{
				"mmm", " s ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.stone_shovel, new Object[]{
				" m ", " s ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.stone_sword, new Object[]{
				" m ", " m ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void ironRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.iron_axe, new Object[]{
				"mm ", "ms ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.iron_hoe, new Object[]{
				"mm ", " s ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.iron_pickaxe, new Object[]{
				"mmm", " s ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.iron_shovel, new Object[]{
				" m ", " s ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.iron_sword, new Object[]{
				" m ", " m ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void diamondRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.diamond_axe, new Object[]{
				"mm ", "ms ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.diamond_hoe, new Object[]{
				"mm ", " s ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.diamond_pickaxe, new Object[]{
				"mmm", " s ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.diamond_shovel, new Object[]{
				" m ", " s ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.diamond_sword, new Object[]{
				" m ", " m ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	//wither resources
	public static ItemWitherWood witherWood = null;
	public static ItemWitherStone witherStone = null;
	public static ItemWitherIron witherIron = null;
	public static ItemWitherDiamond witherDiamond = null;
	
	//wither tools
	//wooden
	
}
