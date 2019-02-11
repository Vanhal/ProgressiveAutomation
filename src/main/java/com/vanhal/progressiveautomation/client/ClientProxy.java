package com.vanhal.progressiveautomation.client;

import com.vanhal.progressiveautomation.client.events.EventRenderWorld;
import com.vanhal.progressiveautomation.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerEntities() {
        super.registerEntities();
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new EventRenderWorld());
    }
}