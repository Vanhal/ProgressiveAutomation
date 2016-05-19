package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.generator.TileGenerator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerGenerator extends BaseContainer {
	
	TileGenerator generator;

	public ContainerGenerator(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 23, 24, false);
		generator = (TileGenerator)entity;
		
		addPlayerInventory(inv, 53);
	}
}
