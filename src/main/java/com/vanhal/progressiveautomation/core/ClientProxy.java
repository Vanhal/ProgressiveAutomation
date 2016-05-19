package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.events.EventRenderWorld;

import net.minecraftforge.common.MinecraftForge;


public class ClientProxy extends Proxy {

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
