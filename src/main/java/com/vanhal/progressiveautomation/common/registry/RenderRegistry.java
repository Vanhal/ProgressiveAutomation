package com.vanhal.progressiveautomation.common.registry;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.api.PABlocks;
import com.vanhal.progressiveautomation.api.PAItems;
import com.vanhal.progressiveautomation.common.util.WrenchModes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = References.MODID, value = Side.CLIENT)
public class RenderRegistry {

    @SubscribeEvent
    public static void onRegister(ModelRegistryEvent event) {
        registerModel(PAItems.MANUAL, 0);
        if (PAConfig.allowWrench) {
            for (WrenchModes.Mode mode : WrenchModes.Mode.values()) {
                registerModel(PAItems.WRENCH, mode.ordinal());
            }
        }
        if (PAConfig.allowWoodenLevel) registerModel(PAItems.WOOD_UPGRADE, 0);
        if ((PAConfig.allowCobbleUpgrade) && (PAConfig.minerEnabled)) registerModel(PAItems.COBBLE_UPGRADE, 0);
        if (PAConfig.allowIronLevel) registerModel(PAItems.IRON_UPGRADE, 0);
        if (PAConfig.allowStoneLevel) registerModel(PAItems.STONE_UPGRADE, 0);
        if (PAConfig.allowDiamondLevel) registerModel(PAItems.DIAMOND_UPGRADE, 0);
        if (PAConfig.allowFillerUpgrade) registerModel(PAItems.FILLER_UPGRADE, 0);
        if (PAConfig.allowWitherUpgrade) registerModel(PAItems.WITHER_UPGRADE, 0);
        if (PAConfig.killerEnabled) {
            if (PAConfig.allowKillPlayer) registerModel(PAItems.FILTER_PLAYER_UPGRADE, 0);
            registerModel(PAItems.FILTER_MOB_UPGRADE, 0);
            registerModel(PAItems.FILTER_ANIMAL_UPGRADE, 0);
            registerModel(PAItems.FILTER_ADULT_UPGRADE, 0);
        }
        if (PAConfig.farmerEnabled) {
            if (PAConfig.allowMilkerUpgrade) {
                registerModel(PAItems.MILKER_UPGRADE, 0);
            }
            if (PAConfig.allowShearingUpgrade) {
                registerModel(PAItems.SHEARING_UPGRADE, 0);
            }
        }
        if (PAConfig.allowCoalPellets) registerModel(PAItems.COAL_PELLET, 0);
        if (PAConfig.rfSupport) registerModel(PAItems.RF_ENGINE, 0);
        registerModel(PAItems.CREATIVE_RF_ENGINE, 0);

        registerModel(PAItems.WITHER_WOOD, 0);
        registerModel(PAItems.WITHER_STONE, 0);
        registerModel(PAItems.WITHER_IRON, 0);
        registerModel(PAItems.WITHER_GOLD, 0);
        registerModel(PAItems.WITHER_DIAMOND, 0);

        registerModel(Item.getItemFromBlock(PABlocks.CAPACITOR_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CAPACITOR_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CAPACITOR_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CAPACITOR_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CHOPPER_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CHOPPER_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CHOPPER_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CHOPPER_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CRAFTER_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CRAFTER_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CRAFTER_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.CRAFTER_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.FARMER_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.FARMER_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.FARMER_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.FARMER_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.GENERATOR_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.GENERATOR_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.GENERATOR_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.GENERATOR_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.KILLER_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.KILLER_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.KILLER_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.KILLER_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.MINER_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.MINER_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.MINER_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.MINER_DIAMOND), 0);
        registerModel(Item.getItemFromBlock(PABlocks.PLANTER_WOODEN), 0);
        registerModel(Item.getItemFromBlock(PABlocks.PLANTER_STONE), 0);
        registerModel(Item.getItemFromBlock(PABlocks.PLANTER_IRON), 0);
        registerModel(Item.getItemFromBlock(PABlocks.PLANTER_DIAMOND), 0);
    }

    private static void registerModel(Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}