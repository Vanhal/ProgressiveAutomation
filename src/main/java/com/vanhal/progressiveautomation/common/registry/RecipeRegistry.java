package com.vanhal.progressiveautomation.common.registry;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.api.PAItems;
import com.vanhal.progressiveautomation.common.items.tools.RecipeWitherTool;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = References.MODID)
public class RecipeRegistry {

    @SubscribeEvent
    public static void onRegister(RegistryEvent.Register<IRecipe> event) {
        //TODO Needs a Resource Location adding to enable Tool Recipes again. - ProxyNeko
        
        //wither_sword
        registerRecipe(event, new RecipeWitherTool(Items.WOODEN_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.STONE_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.IRON_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.GOLDEN_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.DIAMOND_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}));

        //wither_pickaxe
        registerRecipe(event, new RecipeWitherTool(Items.WOODEN_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.STONE_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.IRON_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.GOLDEN_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.DIAMOND_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}));

        //wither_shovel
        registerRecipe(event, new RecipeWitherTool(Items.WOODEN_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.STONE_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.IRON_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.GOLDEN_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.DIAMOND_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}));

        //wither_axe
        registerRecipe(event, new RecipeWitherTool(Items.WOODEN_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.STONE_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.IRON_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.GOLDEN_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.DIAMOND_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}));

        //wither_hoe
        registerRecipe(event, new RecipeWitherTool(Items.WOODEN_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.STONE_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.IRON_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.GOLDEN_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}));
        registerRecipe(event, new RecipeWitherTool(Items.DIAMOND_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}));
        

        if (PAConfig.allowDiamondLevel) {
            registerRecipe(event, new ItemStack(PAItems.WITHER_UPGRADE), "dgd", "gng", "dgd", 'd', PAItems.DIAMOND_UPGRADE, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR);
        } else {
            registerRecipe(event, new ItemStack(PAItems.WITHER_UPGRADE), "dgd", "gng", "dgd", 'd', Blocks.DIAMOND_BLOCK, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR);
        }
    }

    private static void registerRecipe(RegistryEvent.Register<IRecipe> event, RecipeWitherTool recipe) {
    	event.getRegistry().register(recipe);
    }
    
    private static void registerRecipe(RegistryEvent.Register<IRecipe> event, ItemStack stack, Object... recipe) {
        event.getRegistry().register(new ShapedOreRecipe(stack.getItem().getRegistryName(), stack, recipe)
                .setRegistryName(stack.getItem().getRegistryName()));
    }
}