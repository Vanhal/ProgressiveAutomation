package com.vanhal.progressiveautomation.items.tools;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;

import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

public class WitherTools {

	public static void preInit() {
		RecipeSorter.register(Ref.MODID+":witherTool", RecipeWitherTool.class, SHAPED, "after:forge:shapedore");
		
		witherWood = new ItemWitherWood();
		witherStone = new ItemWitherStone();
		witherIron = new ItemWitherIron();
		witherGold = new ItemWitherGold();
		witherDiamond = new ItemWitherDiamond();
		
		witherWood.preInit();
		witherStone.preInit();
		witherIron.preInit();
		witherGold.preInit();
		witherDiamond.preInit();
		
		//add recipes for the tools
		woodenRecipes();
		stoneRecipes();
		ironRecipes();
		goldRecipes();
		diamondRecipes();
	}
	
	public static void init() {
		witherWood.init();
		witherStone.init();
		witherIron.init();
		witherGold.init();
		witherDiamond.init();
	}

	public static void postInit() {

	}
	
	protected static void woodenRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.WOODEN_AXE, new Object[]{
				"mm ", "ms ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.WOODEN_HOE, new Object[]{
				"mm ", " s ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.WOODEN_PICKAXE, new Object[]{
				"mmm", " s ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.WOODEN_SHOVEL, new Object[]{
				" m ", " s ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.WOODEN_SWORD, new Object[]{
				" m ", " m ", " s ", 'm', witherWood, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void stoneRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.STONE_AXE, new Object[]{
				"mm ", "ms ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.STONE_HOE, new Object[]{
				"mm ", " s ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.STONE_PICKAXE, new Object[]{
				"mmm", " s ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.STONE_SHOVEL, new Object[]{
				" m ", " s ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.STONE_SWORD, new Object[]{
				" m ", " m ", " s ", 'm', witherStone, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void ironRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.IRON_AXE, new Object[]{
				"mm ", "ms ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.IRON_HOE, new Object[]{
				"mm ", " s ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.IRON_PICKAXE, new Object[]{
				"mmm", " s ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.IRON_SHOVEL, new Object[]{
				" m ", " s ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.IRON_SWORD, new Object[]{
				" m ", " m ", " s ", 'm', witherIron, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void goldRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.GOLDEN_AXE, new Object[]{
				"mm ", "ms ", " s ", 'm', witherGold, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.GOLDEN_HOE, new Object[]{
				"mm ", " s ", " s ", 'm', witherGold, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.GOLDEN_PICKAXE, new Object[]{
				"mmm", " s ", " s ", 'm', witherGold, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.GOLDEN_SHOVEL, new Object[]{
				" m ", " s ", " s ", 'm', witherGold, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.GOLDEN_SWORD, new Object[]{
				" m ", " m ", " s ", 'm', witherGold, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	protected static void diamondRecipes() {
		RecipeWitherTool recipe = new RecipeWitherTool(Items.DIAMOND_AXE, new Object[]{
				"mm ", "ms ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.DIAMOND_HOE, new Object[]{
				"mm ", " s ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.DIAMOND_PICKAXE, new Object[]{
				"mmm", " s ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.DIAMOND_SHOVEL, new Object[]{
				" m ", " s ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
		recipe = new RecipeWitherTool(Items.DIAMOND_SWORD, new Object[]{
				" m ", " m ", " s ", 'm', witherDiamond, 's', "stickWood"});
		GameRegistry.addRecipe(recipe);
	}
	
	//wither resources
	public static ItemWitherWood witherWood = null;
	public static ItemWitherStone witherStone = null;
	public static ItemWitherIron witherIron = null;
	public static ItemWitherGold witherGold = null;
	public static ItemWitherDiamond witherDiamond = null;
	
	//wither tools
	//wooden
	
}
