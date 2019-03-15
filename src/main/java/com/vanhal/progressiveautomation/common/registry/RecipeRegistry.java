package com.vanhal.progressiveautomation.common.registry;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.api.PAItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegister(RegistryEvent.Register<IRecipe> event) {
        IRecipe wither_upgrade;
        if (PAConfig.allowDiamondLevel) {
            wither_upgrade = new ShapedOreRecipe(null, new ItemStack(PAItems.WITHER_UPGRADE), new Object[]{"dgd", "gng", "dgd", 'd', PAItems.DIAMOND_UPGRADE, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR}).setRegistryName(new ResourceLocation(References.MODID, "wither_upgrade"));
        } else {
            wither_upgrade = new ShapedOreRecipe(null, new ItemStack(PAItems.WITHER_UPGRADE), new Object[]{"dgd", "gng", "dgd", 'd', Blocks.DIAMOND_BLOCK, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR}).setRegistryName(new ResourceLocation(References.MODID, "wither_upgrade"));
        }

        event.getRegistry().register(wither_upgrade);
    }
}