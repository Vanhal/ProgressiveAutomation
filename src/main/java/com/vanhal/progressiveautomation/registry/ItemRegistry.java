package com.vanhal.progressiveautomation.registry;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.api.PABlocks;
import com.vanhal.progressiveautomation.items.ItemCoalPellet;
import com.vanhal.progressiveautomation.items.ItemCreativeRFEngine;
import com.vanhal.progressiveautomation.items.ItemManual;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.items.ItemWrench;
import com.vanhal.progressiveautomation.items.tools.ItemWitherDiamond;
import com.vanhal.progressiveautomation.items.tools.ItemWitherGold;
import com.vanhal.progressiveautomation.items.tools.ItemWitherIron;
import com.vanhal.progressiveautomation.items.tools.ItemWitherStone;
import com.vanhal.progressiveautomation.items.tools.ItemWitherWood;
import com.vanhal.progressiveautomation.items.upgrades.ItemCobbleGenUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemDiamondUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFillerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterAdultUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterAnimalUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterMobUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFilterPlayerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemIronUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemMilkerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemShearingUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemStoneUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWitherUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWoodUpgrade;
import com.vanhal.progressiveautomation.ref.Ref;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Ref.MODID)
public class ItemRegistry {

    @SubscribeEvent
    public static void onRegister(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> items = event.getRegistry();
        items.register(nameItem(new ItemManual(), "manual"));
        if (PAConfig.allowWrench) items.register(nameItem(new ItemWrench(), "wrench"));
        if (PAConfig.allowWoodenLevel) items.register(nameItem(new ItemWoodUpgrade(), "wood_upgrade"));
        if (PAConfig.allowStoneLevel) items.register(nameItem(new ItemStoneUpgrade(), "stone_upgrade"));
        if ((PAConfig.allowCobbleUpgrade) && (PAConfig.minerEnabled))
            items.register(nameItem(new ItemCobbleGenUpgrade(), "cobble_upgrade"));
        if (PAConfig.allowIronLevel) items.register(nameItem(new ItemIronUpgrade(), "iron_upgrade"));
        if (PAConfig.allowDiamondLevel) items.register(nameItem(new ItemDiamondUpgrade(), "diamond_upgrade"));
        if ((PAConfig.allowFillerUpgrade) && (PAConfig.minerEnabled))
            items.register(nameItem(new ItemFillerUpgrade(), "filler_upgrade"));
        if (PAConfig.allowWitherUpgrade) items.register(nameItem(new ItemWitherUpgrade(), "wither_upgrade"));

        if (PAConfig.killerEnabled) {
            if (PAConfig.allowKillPlayer)
                items.register(nameItem(new ItemFilterPlayerUpgrade(), "filter_player_upgrade"));
            items.register(nameItem(new ItemFilterMobUpgrade(), "filter_mob_upgrade"));
            items.register(nameItem(new ItemFilterAnimalUpgrade(), "filter_animal_upgrade"));
            items.register(nameItem(new ItemFilterAdultUpgrade(), "filter_adult_upgrade"));
        }

        if (PAConfig.farmerEnabled) {
            if (PAConfig.allowMilkerUpgrade) items.register(nameItem(new ItemMilkerUpgrade(), "milker_upgrade"));
            if (PAConfig.allowShearingUpgrade) items.register(nameItem(new ItemShearingUpgrade(), "shearing_upgrade"));
        }

        if (PAConfig.allowCoalPellets) items.register(nameItem(new ItemCoalPellet(), "coal_pellet"));
        if (PAConfig.rfSupport) items.register(nameItem(new ItemRFEngine(), "rf_engine"));
        items.register(nameItem(new ItemCreativeRFEngine(), "creative_rf_engine"));

        items.registerAll(
                nameItem(new ItemWitherWood(), "wither_wood"),
                nameItem(new ItemWitherStone(), "wither_stone"),
                nameItem(new ItemWitherIron(), "wither_iron"),
                nameItem(new ItemWitherGold(), "wither_gold"),
                nameItem(new ItemWitherDiamond(), "wither_diamond")
        );

        items.registerAll(
                nameBlockItem(PABlocks.CAPACITOR_WOODEN),
                nameBlockItem(PABlocks.CAPACITOR_STONE),
                nameBlockItem(PABlocks.CAPACITOR_IRON),
                nameBlockItem(PABlocks.CAPACITOR_DIAMOND),
                nameBlockItem(PABlocks.CHOPPER_WOODEN),
                nameBlockItem(PABlocks.CHOPPER_STONE),
                nameBlockItem(PABlocks.CHOPPER_IRON),
                nameBlockItem(PABlocks.CHOPPER_DIAMOND),
                nameBlockItem(PABlocks.CRAFTER_WOODEN),
                nameBlockItem(PABlocks.CRAFTER_STONE),
                nameBlockItem(PABlocks.CRAFTER_IRON),
                nameBlockItem(PABlocks.CRAFTER_DIAMOND),
                nameBlockItem(PABlocks.FARMER_WOODEN),
                nameBlockItem(PABlocks.FARMER_STONE),
                nameBlockItem(PABlocks.FARMER_IRON),
                nameBlockItem(PABlocks.FARMER_DIAMOND),
                nameBlockItem(PABlocks.GENERATOR_WOODEN),
                nameBlockItem(PABlocks.GENERATOR_STONE),
                nameBlockItem(PABlocks.GENERATOR_IRON),
                nameBlockItem(PABlocks.GENERATOR_DIAMOND),
                nameBlockItem(PABlocks.KILLER_WOODEN),
                nameBlockItem(PABlocks.KILLER_STONE),
                nameBlockItem(PABlocks.KILLER_IRON),
                nameBlockItem(PABlocks.KILLER_DIAMOND),
                nameBlockItem(PABlocks.MINER_WOODEN),
                nameBlockItem(PABlocks.MINER_STONE),
                nameBlockItem(PABlocks.MINER_IRON),
                nameBlockItem(PABlocks.MINER_DIAMOND),
                nameBlockItem(PABlocks.PLANTER_WOODEN),
                nameBlockItem(PABlocks.PLANTER_STONE),
                nameBlockItem(PABlocks.PLANTER_IRON),
                nameBlockItem(PABlocks.PLANTER_DIAMOND)
        );
    }

    private static Item nameItem(Item item, String name) {
        item
                .setRegistryName(name)
                .setTranslationKey(Ref.MODID + ":" + name)
                .setCreativeTab(ProgressiveAutomation.PATab);
        return item;
    }

    private static ItemBlock nameBlockItem(Block block) {
        ItemBlock item = new ItemBlock(block);
        ResourceLocation name = block.getRegistryName();
        item.setRegistryName(name);
        return item;
    }
}