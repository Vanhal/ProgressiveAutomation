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
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.gui.slots.SlotBurn;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
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
		
		updateType = ToolHelper.getUpgradeType(miner.getMiningLevel());

		//add slots
		this.addSlotToContainer(new SlotItem(new ItemStack(Blocks.cobblestone), miner, 1, 11, 16)); //cobble
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_PICKAXE,  miner.getMiningLevel(), miner, 2, 37, 52)); //pickaxe
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_SHOVEL, miner.getMiningLevel(), miner, 3, 63, 52)); //shovel
		this.addSlotToContainer(new SlotItem(updateType, miner, 4, 89, 52)); //upgrades

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
            		if (ToolHelper.getLevel(stackInSlot) <= ((TileMiner) entity).getMiningLevel()) {
	            		if (!this.mergeItemStack(stackInSlot, 2, 3, false)) {
	            			return null;
	            		}
            		} else {
            			return null;
            		}
            	} else if ( ToolHelper.getType(stackInSlot.getItem()) == ToolHelper.TYPE_SHOVEL ) {
            		if (ToolHelper.getLevel(stackInSlot) <= ((TileMiner) entity).getMiningLevel()) {
	             		if (!this.mergeItemStack(stackInSlot, 3, 4, false)) {
	             			return null;
	             		}
            		} else {
            			return null;
            		}
             	} else if (TileEntityFurnace.getItemBurnTime(stackInSlot)>0) {
            		if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
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
