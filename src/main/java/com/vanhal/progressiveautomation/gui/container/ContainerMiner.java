package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.gui.slots.SlotBurn;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerMiner extends BaseContainer {
	protected ItemStack updateType;

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		TileMiner miner = (TileMiner) entity;
		
		//ProgressiveAutomation.logger.info("Mining Level: "+miner.getMiningLevel());
		
		updateType = ToolHelper.getUpgradeType(miner.getUpgradeLevel());

		//add slots
		this.addSlotToContainer(new SlotItem(new ItemStack(Blocks.cobblestone), miner, 1, 11, 16)); //cobble
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_PICKAXE,  miner.getUpgradeLevel(), miner, miner.SLOT_PICKAXE, 37, 52)); //pickaxe
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_SHOVEL, miner.getUpgradeLevel(), miner, miner.SLOT_SHOVEL, 63, 52)); //shovel
		this.addSlotToContainer(new SlotItem(updateType, miner, miner.SLOT_UPGRADE, 89, 52)); //upgrades

		//output slots
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START, 112, 16));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 1, 130, 16));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 2, 148, 16));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 3, 112, 34));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 4, 130, 34));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 5, 148, 34));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 6, 112, 52));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 7, 130, 52));
		this.addSlotToContainer(new Slot(miner, miner.SLOT_INVENTORY_START + 8, 148, 52));

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
            	if (stackInSlot.isItemEqual(new ItemStack(Blocks.cobblestone))) {
            		if (!this.mergeItemStack(stackInSlot, 1, 2, false)) {
            			return null;
            		}
            	} else if ( ToolHelper.getType(stackInSlot.getItem()) == ToolHelper.TYPE_PICKAXE ) {
            		if (ToolHelper.getLevel(stackInSlot) <= ((TileMiner) entity).getUpgradeLevel()) {
	            		if (!this.mergeItemStack(stackInSlot, 2, 3, false)) {
	            			return null;
	            		}
            		} else {
            			return null;
            		}
            	} else if ( ToolHelper.getType(stackInSlot.getItem()) == ToolHelper.TYPE_SHOVEL ) {
            		if (ToolHelper.getLevel(stackInSlot) <= ((TileMiner) entity).getUpgradeLevel()) {
	             		if (!this.mergeItemStack(stackInSlot, 3, 4, false)) {
	             			return null;
	             		}
            		} else {
            			return null;
            		}
            	} else if ( (TileEntityFurnace.getItemBurnTime(stackInSlot)>0) || (stackInSlot.getItem() instanceof ItemRFEngine) ) {
            		if (!this.mergeItemStack(stackInSlot, entity.SLOT_FUEL, 1, false)) {
            			return null;
            		}
            	} else if (stackInSlot.isItemEqual(updateType)) {
             		if (!this.mergeItemStack(stackInSlot, 4, 5, false)) {
             			return null;
             		}
             	} else if (!this.mergeItemStack(stackInSlot, 5, 14, false)) {
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
	protected int lastMinedBlocks = -1;
	protected int lastMineBlocks = -1;
	protected int lastUpgrades = -1;
	
	public void sendUpdates(ICrafting i) {
		TileMiner miner = (TileMiner) entity;
		if (lastMineBlocks != miner.getMineBlocks()) {
			lastMineBlocks = miner.getMineBlocks();
			i.sendProgressBarUpdate(this, 2, lastMineBlocks);
		}
		
		if (lastMinedBlocks != miner.getMinedBlocks()) {
			lastMinedBlocks = miner.getMinedBlocks();
			i.sendProgressBarUpdate(this, 3, lastMinedBlocks);
		}
		
		if (lastUpgrades != miner.getUpgrades()) {
			lastUpgrades = miner.getUpgrades();
			i.sendProgressBarUpdate(this, 4, lastUpgrades);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		if (i==2) {
			((TileMiner) entity).setMineBlocks(value);
		} else if (i==3) {
			((TileMiner) entity).setMinedBlocks(value);
		} else if (i==4) {
			((TileMiner) entity).setUpgrades(value);
		}
	}


}
