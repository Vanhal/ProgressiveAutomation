package com.vanhal.progressiveautomation;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.core.Proxy;
import com.vanhal.progressiveautomation.gui.SimpleGuiHandler;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.util.ConfigHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.Version)
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
			return Item.getItemFromBlock(PABlocks.stoneMiner);
		}
	};
	
	//config handler
	public static final ConfigHandler config = new ConfigHandler(Ref.Version);
	
	
	public ProgressiveAutomation() {
		logger.info("Starting automation");
	}
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config.setConfiguration(new Configuration(event.getSuggestedConfigurationFile()));
		PAConfig.init(config);
		
		PAItems.preInit();
		PABlocks.preInit();
		
		config.save();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PAItems.init();
		PABlocks.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PAItems.postInit();
		PABlocks.postInit();
		
		proxy.registerEntities();
		
		config.cleanUp(false, true);
	}
	
}
