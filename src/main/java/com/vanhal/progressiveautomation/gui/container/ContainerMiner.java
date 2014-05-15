package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.TileMiner;

public class ContainerMiner extends BaseContainer {

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity);
		TileMiner miner = (TileMiner) entity;
		
		//add slots
		this.addSlotToContainer(new Slot(miner, 0, 11, 16)); //cobble
		/*this.addSlotToContainer(new Slot(miner, 1, 11, 52)); //burnable
		this.addSlotToContainer(new Slot(miner, 2, 49, 52)); //pickaxe
		this.addSlotToContainer(new Slot(miner, 3, 74, 52)); //shovel
		
		//output slots
		this.addSlotToContainer(new Slot(miner, 4, 112, 16));
		this.addSlotToContainer(new Slot(miner, 5, 130, 16));
		this.addSlotToContainer(new Slot(miner, 6, 148, 16));
		this.addSlotToContainer(new Slot(miner, 7, 112, 34));
		this.addSlotToContainer(new Slot(miner, 8, 130, 34));
		this.addSlotToContainer(new Slot(miner, 9, 148, 34));
		this.addSlotToContainer(new Slot(miner, 10, 112, 52));
		this.addSlotToContainer(new Slot(miner, 11, 130, 52));
		this.addSlotToContainer(new Slot(miner, 12, 148, 52));*/
		
		addPlayerInventory(inv);
	}

	
}
