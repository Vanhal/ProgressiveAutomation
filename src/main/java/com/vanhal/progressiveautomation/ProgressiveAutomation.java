package com.vanhal.progressiveautomation;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

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

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.Version, guiFactory = "com.vanhal.progressiveautomation.gui.PAGuiFactory")
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
			return PAItems.CheatRFEngine;
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
		
		ModHelper.init();
		
		PAItems.preInit();
		PABlocks.preInit();
		
		PAConfig.save();
		MinecraftForge.EVENT_BUS.register(new EventPlayers());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PAItems.init();
		PABlocks.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
		FMLCommonHandler.instance().bus().register(instance);
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
        if(eventArgs.modID.equals(Ref.MODID))
            PAConfig.syncConfig();
    }
	
}
