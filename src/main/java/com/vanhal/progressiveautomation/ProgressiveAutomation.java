package com.vanhal.progressiveautomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.core.Proxy;
import com.vanhal.progressiveautomation.items.PAItems;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.Version)
public class ProgressiveAutomation {
	@Instance(Ref.MODID)
	public static ProgressiveAutomation instance;
	
	@SidedProxy(clientSide = "com.vanhal."+Ref.MODID+".core.ClientProxy", serverSide = "com.vanhal."+Ref.MODID+".core.Proxy")
	public static Proxy proxy;
	
	public static final Logger logger = LogManager.getLogger(Ref.MODID);
	
	
	public ProgressiveAutomation() {
		logger.info("Starting automation");
	}
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PAItems.preInit();
		PABlocks.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PAItems.init();
		PABlocks.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PAItems.postInit();
		PABlocks.postInit();
		
		proxy.registerEntities();
	}
	
}
