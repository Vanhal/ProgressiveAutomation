package com.vanhal.progressiveautomation.gui;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.gui.client.GUIChopper;
import com.vanhal.progressiveautomation.gui.client.GUIGenerator;
import com.vanhal.progressiveautomation.gui.client.GUIMiner;
import com.vanhal.progressiveautomation.gui.container.ContainerChopper;
import com.vanhal.progressiveautomation.gui.container.ContainerGenerator;
import com.vanhal.progressiveautomation.gui.container.ContainerMiner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class SimpleGuiHandler implements IGuiHandler {
	public static int MinerGUI = 1;
	public static int ChopperGUI = 2;
	public static int GeneratorGUI = 3;

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID==MinerGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileMiner) {
				TileMiner tile = (TileMiner)world.getTileEntity(x, y, z);
				return new ContainerMiner(player.inventory, tile);
			}
		} else if (ID==ChopperGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileChopper) {
				TileChopper tile = (TileChopper)world.getTileEntity(x, y, z);
				return new ContainerChopper(player.inventory, tile);
			}
		} else if (ID==GeneratorGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileGenerator) {
				TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
				return new ContainerGenerator(player.inventory, tile);
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
		} else if (ID==ChopperGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileChopper) {
				TileChopper tile = (TileChopper)world.getTileEntity(x, y, z);
				return new GUIChopper(player.inventory, tile);
			}
		} else if (ID==GeneratorGUI) {
			if (world.getTileEntity(x, y, z) instanceof TileGenerator) {
				TileGenerator tile = (TileGenerator)world.getTileEntity(x, y, z);
				return new GUIGenerator(player.inventory, tile);
			}
		}
		return null;
	}

}
