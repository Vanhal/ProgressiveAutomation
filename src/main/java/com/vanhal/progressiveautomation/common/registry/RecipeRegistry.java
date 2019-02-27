package com.vanhal.progressiveautomation.common.registry;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.api.PAItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = References.MODID)
public class RecipeRegistry {

    private static ShapedOreRecipe getWitherRecipe(Item output, Object[] recipe) {
        return getWitherRecipe(new ItemStack(output), recipe);
    }

    private static ShapedOreRecipe getWitherRecipe(ItemStack output, Object[] recipe) {
        return new ShapedOreRecipe(null, output, recipe) {
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
        };
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegister(RegistryEvent.Register<IRecipe> event) {
        //wither_sword
        IRecipe wooden_sword = getWitherRecipe(Items.WOODEN_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "wooden_sword"));
        IRecipe stone_sword = getWitherRecipe(Items.STONE_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "stone_sword"));
        IRecipe iron_sword = getWitherRecipe(Items.IRON_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "iron_sword"));
        IRecipe golden_sword = getWitherRecipe(Items.GOLDEN_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "golden_sword"));
        IRecipe diamond_sword = getWitherRecipe(Items.DIAMOND_SWORD, new Object[]{" m ", " m ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "diamond_sword"));

        //wither_pickaxe
        IRecipe wooden_pickaxe = getWitherRecipe(Items.WOODEN_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "wooden_pickaxe"));
        IRecipe stone_pickaxe = getWitherRecipe(Items.STONE_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "stone_pickaxe"));
        IRecipe iron_pickaxe = getWitherRecipe(Items.IRON_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "iron_pickaxe"));
        IRecipe golden_pickaxe = getWitherRecipe(Items.GOLDEN_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "golden_pickaxe"));
        IRecipe diamond_pickaxe = getWitherRecipe(Items.DIAMOND_PICKAXE, new Object[]{"mmm", " s ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "diamond_pickaxe"));

        //wither_shovel
        IRecipe wooden_shovel = getWitherRecipe(Items.WOODEN_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "wooden_shovel"));
        IRecipe stone_shovel = getWitherRecipe(Items.STONE_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "stone_shovel"));
        IRecipe iron_shovel = getWitherRecipe(Items.IRON_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "iron_shovel"));
        IRecipe golden_shovel = getWitherRecipe(Items.GOLDEN_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "golden_shovel"));
        IRecipe diamond_shovel = getWitherRecipe(Items.DIAMOND_SHOVEL, new Object[]{" m ", " s ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "diamond_shovel"));

        //wither_axe
        IRecipe wooden_axe = getWitherRecipe(Items.WOODEN_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "wooden_axe"));
        IRecipe stone_axe = getWitherRecipe(Items.STONE_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "stone_axe"));
        IRecipe iron_axe = getWitherRecipe(Items.IRON_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "iron_axe"));
        IRecipe golden_axe = getWitherRecipe(Items.GOLDEN_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "golden_axe"));
        IRecipe diamond_axe = getWitherRecipe(Items.DIAMOND_AXE, new Object[]{"mm ", "ms ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "diamond_axe"));

        //wither_hoe
        IRecipe wooden_hoe = getWitherRecipe(Items.WOODEN_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_WOOD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "wooden_hoe"));
        IRecipe stone_hoe = getWitherRecipe(Items.STONE_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_STONE, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "stone_hoe"));
        IRecipe iron_hoe = getWitherRecipe(Items.IRON_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_IRON, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "iron_hoe"));
        IRecipe golden_hoe = getWitherRecipe(Items.GOLDEN_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_GOLD, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "golden_hoe"));
        IRecipe diamond_hoe = getWitherRecipe(Items.DIAMOND_HOE, new Object[]{"mm ", " s ", " s ", 'm', PAItems.WITHER_DIAMOND, 's', "stickWood"}).setRegistryName(new ResourceLocation(References.MODID, "diamond_hoe"));

        IRecipe wither_upgrade;
        if (PAConfig.allowDiamondLevel) {
            wither_upgrade = new ShapedOreRecipe(null, new ItemStack(PAItems.WITHER_UPGRADE), new Object[]{"dgd", "gng", "dgd", 'd', PAItems.DIAMOND_UPGRADE, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR}).setRegistryName(new ResourceLocation(References.MODID, "wither_upgrade"));
        } else {
            wither_upgrade = new ShapedOreRecipe(null, new ItemStack(PAItems.WITHER_UPGRADE), new Object[]{"dgd", "gng", "dgd", 'd', Blocks.DIAMOND_BLOCK, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR}).setRegistryName(new ResourceLocation(References.MODID, "wither_upgrade"));
        }

        event.getRegistry().registerAll(wooden_sword, stone_sword, iron_sword, golden_sword, diamond_sword, wooden_pickaxe, stone_pickaxe, iron_pickaxe, golden_pickaxe, diamond_pickaxe, wooden_shovel, stone_shovel, iron_shovel, golden_shovel, diamond_shovel, wooden_axe, stone_axe, iron_axe, golden_axe, diamond_axe, wooden_hoe, stone_hoe, iron_hoe, golden_hoe, diamond_hoe, wither_upgrade);
    }
}