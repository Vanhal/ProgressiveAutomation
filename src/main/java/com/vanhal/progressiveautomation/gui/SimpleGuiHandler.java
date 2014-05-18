package com.vanhal.progressiveautomation.gui;

import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.gui.client.GUIMiner;
import com.vanhal.progressiveautomation.gui.container.ContainerMiner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class SimpleGuiHandler implements IGuiHandler {
	public static int MinerGUI = 1;

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID==MinerGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileMiner) {
				TileMiner tile = (TileMiner)world.getTileEntity(x, y, z);
				return new ContainerMiner(player.inventory, tile);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID==MinerGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileMiner) {
				TileMiner tile = (TileMiner)world.getTileEntity(x, y, z);
				return new GUIMiner(player.inventory, tile);
			}
		}
		return null;
	}

}
