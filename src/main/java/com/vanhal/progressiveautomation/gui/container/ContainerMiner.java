package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;

public class ContainerMiner extends BaseContainer {

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity);
		
		//add slots
		
		addPlayerInventory(inv);
	}

	
}
