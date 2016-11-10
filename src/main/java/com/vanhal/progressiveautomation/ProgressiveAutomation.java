package com.vanhal.progressiveautomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.blocks.network.NetworkHandler;
import com.vanhal.progressiveautomation.blocks.network.PartialTileNBTUpdateMessage;
import com.vanhal.progressiveautomation.blocks.network.PartialTileNBTUpdateMessageHandler;
import com.vanhal.progressiveautomation.compat.ModHelper;
import com.vanhal.progressiveautomation.core.Proxy;
import com.vanhal.progressiveautomation.events.EventPlayers;
import com.vanhal.progressiveautomation.gui.SimpleGuiHandler;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.Version, guiFactory = "com.vanhal.progressiveautomation.gui.PAGuiFactory", dependencies = "after:CoFHAPI|energy;after:CoFHCore;")
public class ProgressiveAutomation {
	@Instance(Ref.MODID)
	public static ProgressiveAutomation instance;
	
	@SidedProxy(clientSide = "com.vanhal."+Ref.MODID+".core.ClientProxy", serverSide = "com.vanhal."+Ref.MODID+".core.Proxy")
	public static Proxy proxy;
	
	//logger
	public static final Logger logger = LogManager.getLogger(Ref.MODID);
	
	//gui handler
	public static SimpleGuiHandler guiHandler = new SimpleGuiHandler();
	
	//Creative Tab
	public static CreativeTabs PATab = new CreativeTabs("PATab") {
		@Override
		public Item getTabIconItem() {
			return PAItems.cheatRFEngine;
		}
	};
	
	
	
	public ProgressiveAutomation() {
		logger.info("Starting automation");
	}
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkHandler.registerMessageHandler(PartialTileNBTUpdateMessageHandler.class,
				PartialTileNBTUpdateMessage.class, Side.CLIENT);
		
		PAConfig.init(new Configuration(event.getSuggestedConfigurationFile()));
		
		
		
		PAItems.preInit();
		PABlocks.preInit();
		
		PAConfig.save();
		MinecraftForge.EVENT_BUS.register(new EventPlayers());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PAItems.init(event);
		PABlocks.init();
		
		ModHelper.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
		FMLCommonHandler.instance().bus().register(instance);
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PAItems.postInit();
		PABlocks.postInit();
		
		proxy.registerEntities();
		
		PAConfig.postInit();
	}
	
	@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.getModID().equals(Ref.MODID))
            PAConfig.syncConfig();
    }
	
}
