package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitor;
import com.vanhal.progressiveautomation.gui.slots.SlotCharge;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerCapacitor extends BaseContainer {
	
	TileCapacitor capacitor;

	public ContainerCapacitor(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity);
		capacitor = (TileCapacitor)entity;
		
		this.addSlotToContainer(new SlotCharge(capacitor, capacitor.SLOT_CHARGER, 29, 23)); //charger
		
		addPlayerInventory(inv, 53);
	}

}
