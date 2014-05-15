package com.vanhal.progressiveautomation.gui;

import java.lang.reflect.Constructor;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

import com.vanhal.progressiveautomation.gui.client.BaseGUI;

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
		TileEntity tile = world.getTileEntity(x, y, z);
		
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		Class<? extends BaseGUI> guiClass = (Class<? extends BaseGUI>) guis.get(ID);
		Constructor cons = guiClass.getDeclaredConstructor(parameterTypes);
		
		return null;
	}

	
}
