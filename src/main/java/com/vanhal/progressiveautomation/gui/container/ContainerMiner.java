package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;

public class ContainerMiner extends BaseContainer {

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity);
		
		//add slots
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 0, 11, 16)); //cobble
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 1, 11, 52)); //burnable
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 2, 49, 52)); //pickaxe
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 3, 74, 52)); //shovel
		
		//output slots
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 4, 112, 16));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 5, 130, 16));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 6, 148, 16));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 7, 112, 34));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 8, 130, 34));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 9, 148, 34));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 10, 112, 52));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 11, 130, 52));
		this.addSlotToContainer(new Slot((BaseTileEntity)entity, 12, 148, 52));
		
		addPlayerInventory(inv);
	}

	
}
