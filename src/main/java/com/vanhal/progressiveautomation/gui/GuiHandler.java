package com.vanhal.progressiveautomation.gui;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	private final TMap guis = new THashMap();
	private final TMap containers = new THashMap();
	
	public int registerGui(Class gui, Class container) {
		int value = guis.size();
		guis.put(value, gui);
		containers.put(value, container);
		return value;
	}

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	
}
