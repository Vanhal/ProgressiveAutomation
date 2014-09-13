package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.gui.slots.SlotDictionary;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotPlantable;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

public class ContainerPlanter extends BaseContainer {
	
	TilePlanter planter;
	protected ItemStack updateType;

	public ContainerPlanter(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		planter = (TilePlanter) entity;
		
		updateType = ToolHelper.getUpgradeType(planter.getUpgradeLevel());
		
		//add slots
		this.addSlotToContainer(new SlotPlantable(planter, planter.SLOT_SEEDS, 11, 16)); //seeds
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_HOE,  planter.getUpgradeLevel(), planter, planter.SLOT_HOE, 49, 52)); //hoe
		this.addSlotToContainer(new SlotUpgrades(planter.getUpgradeLevel(), planter, planter.SLOT_UPGRADE, 76, 52)); //upgrades

		//output slots
		addInventory(planter, planter.SLOT_INVENTORY_START, 112, 16, 3, 3);

		addPlayerInventory(inv);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		
		if (slotObject!=null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            if (slot < entity.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, entity.getSizeInventory(), inventorySlots.size(), true)) {
                	return null;
                }
            } else {
            	if (planter.isPlantable(stackInSlot)) {
            		if (!this.mergeItemStack(stackInSlot, planter.SLOT_SEEDS, planter.SLOT_SEEDS + 1, false)) {
            			return null;
            		}
            	} else if ( ToolHelper.getType(stackInSlot.getItem()) == ToolHelper.TYPE_HOE ) {
            		if (ToolHelper.getLevel(stackInSlot) <= planter.getUpgradeLevel()) {
	            		if (!this.mergeItemStack(stackInSlot, entity.SLOT_HOE, entity.SLOT_HOE + 1, false)) {
	            			return null;
	            		}
            		} else {
            			return null;
            		}
            	} else if ( (TileEntityFurnace.getItemBurnTime(stackInSlot)>0) || (stackInSlot.getItem() instanceof ItemRFEngine) ) {
            		if (!this.mergeItemStack(stackInSlot, entity.SLOT_FUEL, entity.SLOT_FUEL + 1, false)) {
            			return null;
            		}
            	} else if (stackInSlot.isItemEqual(updateType)) {
             		if (!this.mergeItemStack(stackInSlot, entity.SLOT_UPGRADE, entity.SLOT_UPGRADE + 1, false)) {
             			return null;
             		}
             	} else if (!this.mergeItemStack(stackInSlot, entity.SLOT_INVENTORY_START, entity.SLOT_INVENTORY_END + 1, false)) {
             		return null;
             	}
            }

            if (stackInSlot.stackSize == 0) {
                    slotObject.putStack(null);
            } else {
                    slotObject.onSlotChanged();
            }
		}
		
		return stack;
	}
	
	/* deal with updates */
	protected int lastUpgrades = -1;
	protected int lastStatus = -1;
	
	public void sendUpdates(ICrafting i) {
		if (lastUpgrades != planter.getUpgrades()) {
			lastUpgrades = planter.getUpgrades();
			i.sendProgressBarUpdate(this, 4, lastUpgrades);
		} else if (lastStatus != planter.getStatus()) {
			lastStatus = planter.getStatus();
			i.sendProgressBarUpdate(this, 5, lastStatus);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		if (i==4) {
			planter.setUpgrades(value);
		} else if (i==5) {
			planter.setStatus(value);
		}
	}
}
