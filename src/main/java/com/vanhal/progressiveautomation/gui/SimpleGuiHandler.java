package com.vanhal.progressiveautomation.gui;

import java.lang.reflect.Constructor;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/*
 * Contains some code from cofh Core
 */

public class SimpleGuiHandler implements IGuiHandler {
	private int guiIdCounter = 1;
	public static int manualGUI = 0;
	
	private final TMap containerMap = new THashMap();
	private final TMap guiMap = new THashMap();
	
	public int registerGui(Class gui, Class container) {
		guiIdCounter++;
		guiMap.put(guiIdCounter, gui);
		containerMap.put(guiIdCounter, container);
		return guiIdCounter;
	}
	

	public int registerServerGui(Class container) {
		guiIdCounter++;
		containerMap.put(guiIdCounter, container);
		return guiIdCounter;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (containerMap.containsKey(ID)) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			try {
				if (!world.isRemote) {
					Packet packet = tile.getUpdatePacket();
					if (packet != null) {
						((EntityPlayerMP)player).connection.sendPacket(packet);
					}
					
				}
				Class<? extends Container> containerClass = (Class<? extends Container>) containerMap.get(ID);
				Constructor containerConstructor = containerClass.getDeclaredConstructor(new Class[] { InventoryPlayer.class, TileEntity.class });
				return containerConstructor.newInstance(player.inventory, tile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (guiMap.containsKey(ID)) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			try {
				Class<? extends GuiScreen> guiClass = (Class<? extends GuiScreen>) guiMap.get(ID);
				Constructor guiConstructor = guiClass.getDeclaredConstructor(new Class[] { InventoryPlayer.class, TileEntity.class });
				return guiConstructor.newInstance(player.inventory, tile);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} else if (ID==manualGUI) {
			return null;
		}

		return null;
	}


}
