package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.tileentity.TileEntity;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.TileMiner;

public class ContainerMiner extends BaseContainer {

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity);
		TileMiner miner = (TileMiner) entity;

		//add slots
		this.addSlotToContainer(new CobbleSlot(miner, 0, 11, 16)); //cobble
		this.addSlotToContainer(new BurnSlot(miner, 1, 11, 52)); //burnable
		this.addSlotToContainer(new Slot(miner, 2, 37, 52)); //pickaxe
		this.addSlotToContainer(new Slot(miner, 3, 63, 52)); //shovel
		this.addSlotToContainer(new Slot(miner, 4, 89, 52)); //upgrades

		//output slots
		this.addSlotToContainer(new Slot(miner, 5, 112, 16));
		this.addSlotToContainer(new Slot(miner, 6, 130, 16));
		this.addSlotToContainer(new Slot(miner, 7, 148, 16));
		this.addSlotToContainer(new Slot(miner, 8, 112, 34));
		this.addSlotToContainer(new Slot(miner, 9, 130, 34));
		this.addSlotToContainer(new Slot(miner, 10, 148, 34));
		this.addSlotToContainer(new Slot(miner, 11, 112, 52));
		this.addSlotToContainer(new Slot(miner, 12, 130, 52));
		this.addSlotToContainer(new Slot(miner, 13, 148, 52));

		addPlayerInventory(inv);
	}


}
