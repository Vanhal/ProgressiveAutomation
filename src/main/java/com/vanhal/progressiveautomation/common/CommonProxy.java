package com.vanhal.progressiveautomation.common;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.common.entities.capacitor.TileCapacitor;
import com.vanhal.progressiveautomation.common.entities.capacitor.TileCapacitorDiamond;
import com.vanhal.progressiveautomation.common.entities.capacitor.TileCapacitorIron;
import com.vanhal.progressiveautomation.common.entities.capacitor.TileCapacitorStone;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopperDiamond;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopperIron;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopperStone;
import com.vanhal.progressiveautomation.common.entities.crafter.TileCrafter;
import com.vanhal.progressiveautomation.common.entities.crafter.TileCrafterDiamond;
import com.vanhal.progressiveautomation.common.entities.crafter.TileCrafterIron;
import com.vanhal.progressiveautomation.common.entities.crafter.TileCrafterStone;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmer;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmerDiamond;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmerIron;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmerStone;
import com.vanhal.progressiveautomation.common.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.common.entities.generator.TileGeneratorDiamond;
import com.vanhal.progressiveautomation.common.entities.generator.TileGeneratorIron;
import com.vanhal.progressiveautomation.common.entities.generator.TileGeneratorStone;
import com.vanhal.progressiveautomation.common.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.common.entities.killer.TileKillerDiamond;
import com.vanhal.progressiveautomation.common.entities.killer.TileKillerIron;
import com.vanhal.progressiveautomation.common.entities.killer.TileKillerStone;
import com.vanhal.progressiveautomation.common.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.common.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.common.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.common.entities.miner.TileMinerStone;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanterDiamond;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanterIron;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanterStone;
import com.vanhal.progressiveautomation.References;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void registerEntities() {
        //miner
        GameRegistry.registerTileEntity(TileMiner.class, new ResourceLocation(References.MODID + ":" + "tile_miner"));
        GameRegistry.registerTileEntity(TileMinerStone.class, new ResourceLocation(References.MODID + ":" + "tile_miner_stone"));
        GameRegistry.registerTileEntity(TileMinerIron.class, new ResourceLocation(References.MODID + ":" + "tile_miner_iron"));
        GameRegistry.registerTileEntity(TileMinerDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_miner_diamond"));

        //chopper
        GameRegistry.registerTileEntity(TileChopper.class, new ResourceLocation(References.MODID + ":" + "tile_chopper"));
        GameRegistry.registerTileEntity(TileChopperStone.class, new ResourceLocation(References.MODID + ":" + "tile_chopper_stone"));
        GameRegistry.registerTileEntity(TileChopperIron.class, new ResourceLocation(References.MODID + ":" + "tile_chopper_iron"));
        GameRegistry.registerTileEntity(TileChopperDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_chopper_diamond"));

        //planter
        GameRegistry.registerTileEntity(TilePlanter.class, new ResourceLocation(References.MODID + ":" + "tile_planter"));
        GameRegistry.registerTileEntity(TilePlanterStone.class, new ResourceLocation(References.MODID + ":" + "tile_planter_stone"));
        GameRegistry.registerTileEntity(TilePlanterIron.class, new ResourceLocation(References.MODID + ":" + "tile_planter_iron"));
        GameRegistry.registerTileEntity(TilePlanterDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_planter_diamond"));

        //generator
        GameRegistry.registerTileEntity(TileGenerator.class, new ResourceLocation(References.MODID + ":" + "tile_generator"));
        GameRegistry.registerTileEntity(TileGeneratorStone.class, new ResourceLocation(References.MODID + ":" + "tile_generator_stone"));
        GameRegistry.registerTileEntity(TileGeneratorIron.class, new ResourceLocation(References.MODID + ":" + "tile_generator_iron"));
        GameRegistry.registerTileEntity(TileGeneratorDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_generator_diamond"));

        //generator
        GameRegistry.registerTileEntity(TileCrafter.class, new ResourceLocation(References.MODID + ":" + "tile_crafter"));
        GameRegistry.registerTileEntity(TileCrafterStone.class, new ResourceLocation(References.MODID + ":" + "tile_crafter_stone"));
        GameRegistry.registerTileEntity(TileCrafterIron.class, new ResourceLocation(References.MODID + ":" + "tile_crafter_iron"));
        GameRegistry.registerTileEntity(TileCrafterDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_crafter_diamond"));

        //killer
        GameRegistry.registerTileEntity(TileKiller.class, new ResourceLocation(References.MODID + ":" + "tile_killer"));
        GameRegistry.registerTileEntity(TileKillerStone.class, new ResourceLocation(References.MODID + ":" + "tile_killer_stone"));
        GameRegistry.registerTileEntity(TileKillerIron.class, new ResourceLocation(References.MODID + ":" + "tile_killer_iron"));
        GameRegistry.registerTileEntity(TileKillerDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_killer_diamond"));

        //farmer
        GameRegistry.registerTileEntity(TileFarmer.class, new ResourceLocation(References.MODID + ":" + "tile_farmer"));
        GameRegistry.registerTileEntity(TileFarmerStone.class, new ResourceLocation(References.MODID + ":" + "tile_farmer_stone"));
        GameRegistry.registerTileEntity(TileFarmerIron.class, new ResourceLocation(References.MODID + ":" + "tile_farmer_iron"));
        GameRegistry.registerTileEntity(TileFarmerDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_farmer_diamond"));

        //capacitor
        GameRegistry.registerTileEntity(TileCapacitor.class, new ResourceLocation(References.MODID + ":" + "tile_capacitor_wooden"));
        GameRegistry.registerTileEntity(TileCapacitorStone.class, new ResourceLocation(References.MODID + ":" + "tile_capacitor_stone"));
        GameRegistry.registerTileEntity(TileCapacitorIron.class, new ResourceLocation(References.MODID + ":" + "tile_capacitor_iron"));
        GameRegistry.registerTileEntity(TileCapacitorDiamond.class, new ResourceLocation(References.MODID + ":" + "tile_capacitor_diamond"));
    }

    public int registerGui(String guiName) {
        return registerGui(guiName, guiName);
    }

    public int registerGui(String guiName, String containerName) {
        Class<?> gui = null;
        Class<?> container = null;
        try {
            gui = CommonProxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.client.gui.GUI" + guiName);
        } catch (ClassNotFoundException e) {
        }

        try {
            container = CommonProxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.client.gui.container.Container" + containerName);
        } catch (ClassNotFoundException e) {
            return -1;
        }

        if (gui == null) {
            return ProgressiveAutomation.guiHandler.registerServerGui(container);
        } else {
            return ProgressiveAutomation.guiHandler.registerGui(gui, container);
        }

    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return true;
    }

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }
}