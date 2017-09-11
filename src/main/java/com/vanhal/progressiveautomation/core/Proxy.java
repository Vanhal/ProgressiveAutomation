package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitor;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitorDiamond;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitorIron;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitorStone;
import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperDiamond;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperIron;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperStone;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafter;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterDiamond;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterIron;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterStone;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmer;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmerDiamond;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmerIron;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmerStone;
import com.vanhal.progressiveautomation.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.entities.generator.TileGeneratorDiamond;
import com.vanhal.progressiveautomation.entities.generator.TileGeneratorIron;
import com.vanhal.progressiveautomation.entities.generator.TileGeneratorStone;
import com.vanhal.progressiveautomation.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.entities.killer.TileKillerDiamond;
import com.vanhal.progressiveautomation.entities.killer.TileKillerIron;
import com.vanhal.progressiveautomation.entities.killer.TileKillerStone;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.entities.miner.TileMinerStone;
import com.vanhal.progressiveautomation.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterDiamond;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterIron;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterStone;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		//miner
		GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
		GameRegistry.registerTileEntity(TileMinerStone.class, "TileMinerStone");
		GameRegistry.registerTileEntity(TileMinerIron.class, "TileMinerIron");
		GameRegistry.registerTileEntity(TileMinerDiamond.class, "TileMinerDiamond");
		
		//chopper
		GameRegistry.registerTileEntity(TileChopper.class, "TileChopper");
		GameRegistry.registerTileEntity(TileChopperStone.class, "TileChopperStone");
		GameRegistry.registerTileEntity(TileChopperIron.class, "TileChopperIron");
		GameRegistry.registerTileEntity(TileChopperDiamond.class, "TileChopperDiamond");
		
		//planter
		GameRegistry.registerTileEntity(TilePlanter.class, "TilePlanter");
		GameRegistry.registerTileEntity(TilePlanterStone.class, "TilePlanterStone");
		GameRegistry.registerTileEntity(TilePlanterIron.class, "TilePlanterIron");
		GameRegistry.registerTileEntity(TilePlanterDiamond.class, "TilePlanterDiamond");
		
		//generator
		GameRegistry.registerTileEntity(TileGenerator.class, "TileGenerator");
		GameRegistry.registerTileEntity(TileGeneratorStone.class, "TileGeneratorStone");
		GameRegistry.registerTileEntity(TileGeneratorIron.class, "TileGeneratorIron");
		GameRegistry.registerTileEntity(TileGeneratorDiamond.class, "TileGeneratorDiamond");
		
		//generator
		GameRegistry.registerTileEntity(TileCrafter.class, "TileCrafter");
		GameRegistry.registerTileEntity(TileCrafterStone.class, "TileCrafterStone");
		GameRegistry.registerTileEntity(TileCrafterIron.class, "TileCrafterIron");
		GameRegistry.registerTileEntity(TileCrafterDiamond.class, "TileCrafterDiamond");
		
		//killer
		GameRegistry.registerTileEntity(TileKiller.class, "TileKiller");
		GameRegistry.registerTileEntity(TileKillerStone.class, "TileKillerStone");
		GameRegistry.registerTileEntity(TileKillerIron.class, "TileKillerIron");
		GameRegistry.registerTileEntity(TileKillerDiamond.class, "TileKillerDiamond");
		
		//farmer
		GameRegistry.registerTileEntity(TileFarmer.class, "TileFarmer");
		GameRegistry.registerTileEntity(TileFarmerStone.class, "TileFarmerStone");
		GameRegistry.registerTileEntity(TileFarmerIron.class, "TileFarmerIron");
		GameRegistry.registerTileEntity(TileFarmerDiamond.class, "TileFarmerDiamond");
		
		//capacitor
		GameRegistry.registerTileEntity(TileCapacitor.class, "TileCapacitor");
		GameRegistry.registerTileEntity(TileCapacitorStone.class, "TileCapacitorStone");
		GameRegistry.registerTileEntity(TileCapacitorIron.class, "TileCapacitorIron");
		GameRegistry.registerTileEntity(TileCapacitorDiamond.class, "TileCapacitorDiamond");
	}
	
	public int registerGui(String guiName) {
		return registerGui(guiName, guiName);
	}
	
	public int registerGui(String guiName, String containerName) {
		Class<?> gui = null;
		Class<?> container = null;
		try {
			gui = Proxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.gui.client.GUI" + guiName);
		} catch (ClassNotFoundException e) {
			
		}
		try {
			container = Proxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.gui.container.Container" + containerName);
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
