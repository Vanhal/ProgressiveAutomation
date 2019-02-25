package com.vanhal.progressiveautomation;

import com.vanhal.progressiveautomation.api.PAItems;
import com.vanhal.progressiveautomation.client.events.EventPlayers;
import com.vanhal.progressiveautomation.client.gui.SimpleGuiHandler;
import com.vanhal.progressiveautomation.common.CommonProxy;
import com.vanhal.progressiveautomation.common.compat.ModHelper;
import com.vanhal.progressiveautomation.common.network.NetworkHandler;
import com.vanhal.progressiveautomation.common.network.PartialTileNBTUpdateMessage;
import com.vanhal.progressiveautomation.common.network.PartialTileNBTUpdateMessageHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
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
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = References.MODID,
        name = References.MODNAME,
        version = References.Version,
        guiFactory = "com.vanhal.progressiveautomation.client.PAGuiFactory",
        dependencies = "after:CoFHAPI|energy;after:CoFHCore;")
public class ProgressiveAutomation {

    @Instance(References.MODID)
    public static ProgressiveAutomation instance;

    @SidedProxy(
            clientSide = "com.vanhal." + References.MODID + ".client.ClientProxy",
            serverSide = "com.vanhal." + References.MODID + ".common.CommonProxy"
    )
    public static CommonProxy proxy;

    // logger
    public static final Logger logger = LogManager.getLogger(References.MODID);

    // gui handler
    public static SimpleGuiHandler guiHandler = new SimpleGuiHandler();

    // Creative Tab
    public static CreativeTabs PATab = new CreativeTabs("PATab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(PAItems.CREATIVE_RF_ENGINE);
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

        PAConfig.save();
        MinecraftForge.EVENT_BUS.register(new EventPlayers());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModHelper.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
        MinecraftForge.EVENT_BUS.register(instance);
        proxy.init();
        //TODO ore dict so all recipes can use stone and the sub blocks of stone or see what Dark is doing in BookShelf to fix this. - ProxyNeko
        // Added so that any coloured wool can be used in our recipes.
        OreDictionary.registerOre("blockWool", Blocks.WOOL);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.registerEntities();
        PAConfig.postInit();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(References.MODID))
            PAConfig.syncConfig();
    }
    
}