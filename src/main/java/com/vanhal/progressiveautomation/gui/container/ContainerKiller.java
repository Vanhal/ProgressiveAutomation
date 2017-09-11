package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerKiller extends BaseContainer {
	
	protected TileKiller killer;
	protected ItemStack updateType;
	
	public ContainerKiller(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 40);
		killer = (TileKiller) entity;
		
		updateType = ToolHelper.getUpgradeType(killer.getUpgradeLevel());
		
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_SWORD,  killer.getUpgradeLevel(), killer, killer.SLOT_SWORD, 49, 52)); //sword
		this.addSlotToContainer(new SlotUpgrades(killer, killer.SLOT_UPGRADE, 76, 52)); //upgrades
		
		//output slots
		addInventory(killer, killer.SLOT_INVENTORY_START, 112, 16, 3, 3);

		addPlayerInventory(inv);
	}

	
}
