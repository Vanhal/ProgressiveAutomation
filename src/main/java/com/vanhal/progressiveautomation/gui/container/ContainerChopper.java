package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.TileChopper;
import com.vanhal.progressiveautomation.gui.slots.SlotDictionary;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerChopper extends BaseContainer {
	protected ItemStack updateType;

	public ContainerChopper(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		TileChopper chopper = (TileChopper) entity;
		
		updateType = ToolHelper.getUpgradeType(chopper.getUpgradeLevel());

		//add slots
		this.addSlotToContainer(new SlotDictionary(new ItemStack(Blocks.sapling), chopper, chopper.SLOT_SAPLINGS, 11, 16)); //saplings
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_AXE,  chopper.getUpgradeLevel(), chopper, chopper.SLOT_AXE, 49, 52)); //axe
		this.addSlotToContainer(new SlotItem(updateType, chopper, chopper.SLOT_UPGRADE, 76, 52)); //upgrades

		//output slots
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START, 112, 16));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 1, 130, 16));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 2, 148, 16));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 3, 112, 34));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 4, 130, 34));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 5, 148, 34));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 6, 112, 52));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 7, 130, 52));
		this.addSlotToContainer(new Slot(chopper, chopper.SLOT_INVENTORY_START + 8, 148, 52));

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
            	if (OreDictionary.getOreID(stackInSlot) == OreDictionary.getOreID("treeSapling")) {
            		if (!this.mergeItemStack(stackInSlot, ((TileChopper) entity).SLOT_SAPLINGS, ((TileChopper) entity).SLOT_SAPLINGS + 1, false)) {
            			return null;
            		}
            	} else if ( ToolHelper.getType(stackInSlot.getItem()) == ToolHelper.TYPE_AXE ) {
            		if (ToolHelper.getLevel(stackInSlot) <= ((TileChopper) entity).getUpgradeLevel()) {
	            		if (!this.mergeItemStack(stackInSlot, entity.SLOT_AXE, entity.SLOT_AXE + 1, false)) {
	            			return null;
	            		}
            		} else {
            			return null;
            		}
            	} else if (TileEntityFurnace.getItemBurnTime(stackInSlot)>0) {
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
	
	public void sendUpdates(ICrafting i) {
		TileChopper chopper = (TileChopper) entity;
		
		if (lastUpgrades != chopper.getUpgrades()) {
			lastUpgrades = chopper.getUpgrades();
			i.sendProgressBarUpdate(this, 4, lastUpgrades);
		}
		
		if (chopper.isChopping() != chopper.chopping) {
			chopper.chopping = chopper.isChopping();
			i.sendProgressBarUpdate(this, 5, (chopper.chopping)?1:0);
		}

		if (chopper.isPlanting() != chopper.planting) {
			chopper.planting = chopper.isPlanting();
			i.sendProgressBarUpdate(this, 6, (chopper.planting)?1:0);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		TileChopper chopper = (TileChopper) entity;
		if (i==4) {
			chopper.setUpgrades(value);
		} else if (i==5) {
			chopper.chopping = (value==1);
		} else if (i==6) {
			chopper.planting = (value==1);
		}
	}
	
}
