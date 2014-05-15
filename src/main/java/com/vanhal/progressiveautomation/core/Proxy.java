package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;

import cpw.mods.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
	}

	public int registerGui(String name) {
		try {
			Class<?> gui = Proxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.gui.client.GUI"+name);
			Class<?> container = Proxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.gui.container.Container"+name);
			return ProgressiveAutomation.guiHandler.registerGui(gui, container);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
}
